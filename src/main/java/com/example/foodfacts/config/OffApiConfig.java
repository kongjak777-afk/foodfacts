package com.example.foodfacts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

/**
 * Open Food Facts API 호출에 필요한 공통 설정을 모아두는 Config.
 * - baseUrl
 * - User-Agent 헤더
 *
 * 이렇게 분리하면 나중에 API 주소 변경/헤더 추가 등이 쉬워집니다.
 */
@Configuration
public class OffApiConfig {

    @Bean
    public RestClient offRestClient(
            @Value("${off.api.base-url}") String baseUrl,
            @Value("${off.api.user-agent}") String userAgent
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                // 모든 요청에 User-Agent 기본 추가
                .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                .build();
    }
}
