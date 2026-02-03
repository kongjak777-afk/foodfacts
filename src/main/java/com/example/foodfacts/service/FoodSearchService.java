package com.example.foodfacts.service;

import com.example.foodfacts.api.OpenFoodFactsApiClient;
import com.example.foodfacts.dto.OffProductSummaryDto;
import com.example.foodfacts.dto.OffSearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * "검색/조회" 비즈니스 로직 담당
 * - Controller는 요청/응답만
 * - Service는 실제 로직 (정렬/가공/예외처리 등)
 */
@Service
public class FoodSearchService {

    private final OpenFoodFactsApiClient apiClient;

    public FoodSearchService(OpenFoodFactsApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<OffProductSummaryDto> getProductsByCategory(String categoryEn) {
        // 초보 단계에서는 pageSize를 고정해도 좋습니다.
        int pageSize = 12;

        OffSearchResponseDto response = apiClient.searchByCategory(categoryEn, pageSize);

        // API 데이터는 누락이 있을 수 있으니 null 방어를 해줍니다.
        if (response == null || response.products() == null) {
            return List.of();
        }
        return response.products();
    }
}
