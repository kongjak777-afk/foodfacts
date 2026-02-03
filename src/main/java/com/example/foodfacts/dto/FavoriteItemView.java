package com.example.foodfacts.dto;

/**
 * 즐겨찾기 목록 화면용 뷰 모델
 * - DB 엔티티(Favorite)를 그대로 뿌리지 않고, 화면에 필요한 값만 모아서 사용
 */
public record FavoriteItemView(
        String productCode,
        String displayName,
        String imageUrl,
        String createdAtText
) { }
