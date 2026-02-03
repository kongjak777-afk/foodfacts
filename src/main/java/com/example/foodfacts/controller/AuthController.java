package com.example.foodfacts.controller;

import com.example.foodfacts.dto.RegisterRequestDto;
import com.example.foodfacts.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 회원가입 + 로그인 페이지 제공
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDto("", ""));
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequestDto req,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            authService.register(req);
        } catch (IllegalArgumentException e) {
            // 중복 아이디 등 사용자 입력 문제
            bindingResult.reject("registerFail", e.getMessage());
            return "register";
        }

        // 가입 후 로그인 페이지로
        return "redirect:/login";
    }
}
