package com.example.foodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 검색 결과의 제품 1개(요약) DTO
 * - code: 바코드(제품 식별자)
 * - product_name: 제품명
 * - brands: 브랜드 문자열
 * - nutriscore_grade: 영양등급(a~e 등)
 *
 * @JsonProperty: JSON 필드명이 자바 변수명과 다를 때 매핑
 * @JsonIgnoreProperties(ignoreUnknown = true): 우리가 안 쓰는 필드는 무시(안전)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OffProductSummaryDto(
        String code,

        @JsonProperty("product_name")
        String productName,

        String brands,

        @JsonProperty("nutriscore_grade")
        String nutriScoreGrade
) { }
