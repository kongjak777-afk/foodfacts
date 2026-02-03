package com.example.foodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * /api/v2/product/{code} 응답 래퍼
 * - product 필드에 OffProductDetailDto가 들어옵니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OffProductDetailResponseDto(
        OffProductDetailDto product
) { }
