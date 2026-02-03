package com.example.foodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * v2 Search API 응답 DTO
 * - count: 결과 개수
 * - products: 제품 목록
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OffSearchResponseDto(
        Integer count,
        List<OffProductSummaryDto> products
) { }
