package com.example.foodfacts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 폼 입력값
 * - username/password만 받는다(개인정보 최소)
 */
public record RegisterRequestDto(
        @NotBlank(message = "아이디(닉네임)를 입력하세요.")
        @Size(min = 3, max = 30, message = "아이디는 3~30자여야 합니다.")
        String username,

        @NotBlank(message = "비밀번호를 입력하세요.")
        @Size(min = 6, max = 50, message = "비밀번호는 6~50자여야 합니다.")
        String password
) { }
