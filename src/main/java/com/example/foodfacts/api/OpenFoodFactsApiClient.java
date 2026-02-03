package com.example.foodfacts.api;

import com.example.foodfacts.dto.OffProductDetailResponseDto;
import com.example.foodfacts.dto.OffSearchResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Open Food Facts API 호출만 담당하는 클래스.
 * - "HTTP 통신"을 한 곳에 모아두면, 나중에 서비스/컨트롤러가 깔끔해집니다.
 */
@Component
public class OpenFoodFactsApiClient {

    private final RestClient offRestClient;

    public OpenFoodFactsApiClient(RestClient offRestClient) {
        this.offRestClient = offRestClient;
    }

    /**
     * 카테고리(영문 태그)로 제품을 검색합니다.
     *
     * NOTE:
     * - v2 search endpoint: /api/v2/search
     * - fields 파라미터로 필요한 필드만 가져오면 응답이 가벼워져서 빠릅니다.
     */
    public OffSearchResponseDto searchByCategory(String categoryEn, int pageSize) {
        // URL 쿼리 파라미터 조립 (문자열로 덕지덕지 붙이지 않기 위해 Builder 사용)
        String pathAndQuery = UriComponentsBuilder
                .fromPath("/api/v2/search")
                // 카테고리 필터: categories_tags_en 사용 예시는 공식 튜토리얼에 나옵니다.
                // 공백이 들어가도 자동 인코딩됩니다.
                .queryParam("categories_tags_en", categoryEn)
                // 한 번에 몇 개 가져올지
                .queryParam("page_size", pageSize)
                // 응답에서 필요한 필드만 요청 (성능/가독성)
                .queryParam("fields", "code,product_name,brands,nutriscore_grade")
                .build()
                .toUriString();

        return offRestClient.get()
                .uri(pathAndQuery)
                .retrieve()
                .body(OffSearchResponseDto.class);
    }

    /**
     * 바코드(code)로 제품 상세 조회
     *
     * - v2 product endpoint: /api/v2/product/{barcode}
     * - fields 파라미터로 필요한 필드만 받으면 응답이 작고 빠릅니다.
     * - lc 파라미터로 선호 언어를 지정할 수 있습니다(가능한 범위에서).  (언어 데이터가 없는 제품도 많음)
     */
    public OffProductDetailResponseDto getProductDetail(String code, String lang) {
        String pathAndQuery = UriComponentsBuilder
                .fromPath("/api/v2/product/{code}")
                .queryParam("fields",
                        "code,product_name,product_name_ko,brands," +
                                "ingredients_text,ingredients_text_ko,image_url," +
                                "nutriscore_grade,nutriments"
                )
                // 선호 언어 (ko 등). 모든 필드가 번역되는 건 아니고, 데이터가 존재할 때만 유효합니다.
                .queryParam("lc", lang)
                .buildAndExpand(code)
                .toUriString();

        return offRestClient.get()
                .uri(pathAndQuery)
                .retrieve()
                .body(OffProductDetailResponseDto.class);
    }
}
