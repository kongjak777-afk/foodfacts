package com.example.foodfacts.service;

import com.example.foodfacts.dto.OffProductDetailDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 매우 단순한 메모리 캐시 서비스 (연습용)
 *
 * - key = code + ":" + lang
 * - 일정 시간(TTL) 동안 같은 요청이면 외부 API 호출 없이 캐시 값을 반환
 *
 * 주의:
 * - 서버 재시작하면 캐시는 사라짐
 * - 운영 환경에서는 Redis 같은 외부 캐시를 쓰는 것이 일반적
 */
@Service
public class ProductCacheService {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final FoodDetailService foodDetailService;

    // thread-safe map
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public ProductCacheService(FoodDetailService foodDetailService) {
        this.foodDetailService = foodDetailService;
    }

    public Optional<OffProductDetailDto> getDetailCached(String code, String lang) {
        String key = code + ":" + lang;

        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return Optional.ofNullable(entry.value);
        }

        // 캐시 miss 또는 만료 -> 외부 호출
        Optional<OffProductDetailDto> fresh = foodDetailService.getDetail(code, lang);

        // 값이 없더라도 캐싱(짧게)하면 같은 잘못된 code 반복 호출을 줄일 수 있음
        cache.put(key, new CacheEntry(fresh.orElse(null), Instant.now()));

        return fresh;
    }

    private static class CacheEntry {
        private final OffProductDetailDto value;
        private final Instant savedAt;

        private CacheEntry(OffProductDetailDto value, Instant savedAt) {
            this.value = value;
            this.savedAt = savedAt;
        }

        private boolean isExpired() {
            return savedAt.plus(TTL).isBefore(Instant.now());
        }
    }
}
