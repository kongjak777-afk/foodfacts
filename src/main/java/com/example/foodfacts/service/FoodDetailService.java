package com.example.foodfacts.service;

import com.example.foodfacts.api.OpenFoodFactsApiClient;
import com.example.foodfacts.dto.OffProductDetailDto;
import com.example.foodfacts.dto.OffProductDetailResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 제품 상세 조회 서비스
 * - API 호출 + null-safe + "표시용 값(한글 우선)" 가공을 담당합니다.
 */
@Service
public class FoodDetailService {

    private final OpenFoodFactsApiClient apiClient;

    public FoodDetailService(OpenFoodFactsApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Optional<OffProductDetailDto> getDetail(String code, String lang) {
        OffProductDetailResponseDto response = apiClient.getProductDetail(code, lang);

        if (response == null || response.product() == null) {
            return Optional.empty();
        }
        return Optional.of(response.product());
    }

    /**
     * 화면에 보여줄 "제품명"을 결정합니다.
     * - lang=ko 일 때 productNameKo가 있으면 우선 사용
     * - 없으면 productName 사용
     * - 둘 다 없으면 "(이름 정보 없음)"
     */
    public String resolveDisplayName(OffProductDetailDto p, String lang) {
        if ("ko".equalsIgnoreCase(lang) && hasText(p.productNameKo())) {
            return p.productNameKo();
        }
        if (hasText(p.productName())) {
            return p.productName();
        }
        return "(이름 정보 없음)";
    }

    /**
     * 성분 텍스트도 같은 규칙(한글 우선)
     */
    public String resolveDisplayIngredients(OffProductDetailDto p, String lang) {
        if ("ko".equalsIgnoreCase(lang) && hasText(p.ingredientsTextKo())) {
            return p.ingredientsTextKo();
        }
        if (hasText(p.ingredientsText())) {
            return p.ingredientsText();
        }
        return "(성분 정보 없음)";
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
