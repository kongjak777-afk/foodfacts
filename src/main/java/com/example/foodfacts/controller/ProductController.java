package com.example.foodfacts.controller;

import com.example.foodfacts.dto.CommentCreateRequestDto;          // ===== [추가] 댓글 폼 DTO =====
import com.example.foodfacts.service.CommentService;               // ===== [추가] 댓글 서비스 =====
import com.example.foodfacts.service.FoodDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.foodfacts.service.FavoriteService;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final FoodDetailService foodDetailService;

    // ===========================
    // ===== [추가/수정] 시작 =====
    // ===========================
    /**
     * 댓글 목록을 가져오고(읽기), 상세 페이지에 댓글 폼을 제공하기 위해 CommentService를 주입합니다.
     */
    private final CommentService commentService;

    private final FavoriteService favoriteService;

    public ProductController(FoodDetailService foodDetailService,
                             CommentService commentService,
                             FavoriteService favoriteService) {
        this.foodDetailService = foodDetailService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
    }
    // =========================
    // ===== [추가/수정] 끝 =====
    // =========================

    /**
     * 제품 상세 페이지
     * <p>
     * 예)
     * - /products/3017620422003?lang=ko
     * - /products/3017620422003?lang=en
     */
    @GetMapping("/{code}")
    public String detail(
            @PathVariable String code,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            Authentication authentication,
            Model model
    ) {
        // 1) 외부 API에서 제품 상세 조회
        var opt = foodDetailService.getDetail(code, lang);

        // 2) 제품이 없으면 not-found 화면으로
        if (opt.isEmpty()) {
            model.addAttribute("code", code);
            model.addAttribute("lang", lang);
            return "product-not-found";
        }

        var p = opt.get();

        // 3) 화면 표시용(한글 우선) 텍스트 결정
        model.addAttribute("lang", lang);
        model.addAttribute("product", p);
        model.addAttribute("displayName", foodDetailService.resolveDisplayName(p, lang));
        model.addAttribute("displayIngredients", foodDetailService.resolveDisplayIngredients(p, lang));


        /**
         * [핵심1] 댓글 목록을 DB에서 조회해서 상세 페이지에 전달
         * - productCode = 바코드(code)
         * - 최신순으로 가져오도록 Repository에서 정렬해둔 상태
         */
        var comments = commentService.getComments(code);
        model.addAttribute("comments", comments);

        /**
         * [핵심2] 댓글 작성 폼 객체를 만들어서 화면에 전달
         * - Thymeleaf에서 th:object="${commentForm}" 로 바인딩할 때 필요
         * - 로그인 여부에 따라 폼이 보이거나 숨겨지도록(템플릿에서 처리)
         */
        model.addAttribute("commentForm", new CommentCreateRequestDto(""));


        boolean favorited = false;

        // 로그인한 경우에만 즐겨찾기 상태 조회 (익명은 false)
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            favorited = favoriteService.isFavorited(authentication.getName(), code);
        }

        model.addAttribute("favorited", favorited);


        // 4) templates/product-detail.html 렌더링
        return "product-detail";
    }
}
