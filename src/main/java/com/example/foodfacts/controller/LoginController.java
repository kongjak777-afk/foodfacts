package com.example.foodfacts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 커스텀 로그인 페이지 제공용
 * - 실제 로그인 처리는 Spring Security가 /login POST로 처리합니다.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
