package com.example.foodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * 제품 상세 DTO (우리가 화면에 보여줄 최소 필드만)
 * - Open Food Facts 데이터는 제품마다 필드가 없을 수 있어 null-safe 전제가 필요합니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OffProductDetailDto(
        String code,

        @JsonProperty("product_name")
        String productName,          // 기본 제품명(언어 일반)

        @JsonProperty("product_name_ko")
        String productNameKo,        // 한국어 제품명(있을 수도, 없을 수도)

        String brands,

        @JsonProperty("ingredients_text")
        String ingredientsText,      // 기본 성분 텍스트

        @JsonProperty("ingredients_text_ko")
        String ingredientsTextKo,    // 한국어 성분 텍스트(있을 수도)

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("nutriscore_grade")
        String nutriScoreGrade,

        // nutriments는 종류가 많아 Map으로 받는 게 초보 단계에선 제일 안전합니다.
        Map<String, Object> nutriments
) { }
