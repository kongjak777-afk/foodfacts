package com.example.foodfacts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 댓글 입력 폼
 */
public record CommentCreateRequestDto(
        @NotBlank(message = "댓글 내용을 입력하세요.")
        @Size(min = 1, max = 500, message = "댓글은 1~500자까지 가능합니다.")
        String content
) { }
