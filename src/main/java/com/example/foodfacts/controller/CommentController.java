package com.example.foodfacts.controller;

import com.example.foodfacts.dto.CommentCreateRequestDto;
import com.example.foodfacts.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 작성만 담당 (POST)
 * - 읽기는 상세 페이지에서 Service로 조회해서 모델에 담습니다.
 */
@Controller
@RequestMapping("/products")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{code}/comments")
    public String addComment(
            @PathVariable String code,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            @Valid @ModelAttribute("commentForm") CommentCreateRequestDto form,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        // 폼 에러가 있으면 상세 페이지로 돌아가서 에러를 보여주는 게 이상적이지만,
        // 초보 단계에서는 간단히 redirect만 합니다(추후 개선).
        if (bindingResult.hasErrors()) {
            return "redirect:/products/" + code + "?lang=" + lang;
        }

        // 현재 로그인 username
        String username = authentication.getName();

        commentService.addComment(code, username, form.content());

        return "redirect:/products/" + code + "?lang=" + lang;
    }


    /**
     * 댓글 삭제 (POST 방식)
     *
     * URL 예:
     *  POST /products/3017620422003/comments/10/delete?lang=ko
     */
    @PostMapping("/{code}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable String code,
            @PathVariable Long commentId,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            Authentication authentication
    ) {
        String username = authentication.getName();

        // 서비스에서 "본인 여부"를 최종 검증
        try {
            commentService.deleteComment(code, commentId, username);
        } catch (IllegalArgumentException e) {
            // 초보 단계에서는 간단히 무시하고 redirect
            // (추후: 에러 메시지를 상세 페이지에 표시하도록 개선 가능)
        }

        return "redirect:/products/" + code + "?lang=" + lang;
    }
}
