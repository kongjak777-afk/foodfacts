package com.example.foodfacts.service;

import com.example.foodfacts.domain.Favorite;
import com.example.foodfacts.domain.UserAccount;
import com.example.foodfacts.repository.FavoriteRepository;
import com.example.foodfacts.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.foodfacts.dto.FavoriteItemView;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final UserAccountRepository userRepo;

    // ===== [추가] 제품 상세 캐시 서비스 =====
    private final ProductCacheService productCacheService;

    public FavoriteService(FavoriteRepository favoriteRepo,
                           UserAccountRepository userRepo,
                           ProductCacheService productCacheService) {
        this.favoriteRepo = favoriteRepo;
        this.userRepo = userRepo;
        this.productCacheService = productCacheService;
    }


    @Transactional(readOnly = true)
    public boolean isFavorited(String username, String productCode) {
        UserAccount user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));
        return favoriteRepo.existsByUserAndProductCode(user, productCode);
    }

    @Transactional
    public void add(String username, String productCode) {
        UserAccount user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));

        if (favoriteRepo.existsByUserAndProductCode(user, productCode)) {
            return; // 이미 즐겨찾기면 무시(멱등)
        }

        favoriteRepo.save(new Favorite(user, productCode));
    }

    @Transactional
    public void remove(String username, String productCode) {
        UserAccount user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));

        favoriteRepo.findByUserAndProductCode(user, productCode)
                .ifPresent(favoriteRepo::delete);
    }

    @Transactional(readOnly = true)
    public List<Favorite> list(String username) {
        UserAccount user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));
        return favoriteRepo.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public java.util.List<FavoriteItemView> listView(String username, String lang) {
        UserAccount user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));

        var favorites = favoriteRepo.findByUserOrderByCreatedAtDesc(user);

        // 날짜 포맷(화면용)
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        var result = new ArrayList<FavoriteItemView>();

        for (var f : favorites) {
            String code = f.getProductCode();

            // 캐시를 통해 외부 API 호출 최소화
            var opt = productCacheService.getDetailCached(code, lang);

            // 제품명은 한글 우선 (FoodDetailService 로직을 재활용하고 싶으면 그쪽 메서드를 또 호출해도 됨)
            String name = opt.map(p -> {
                // 한국어 우선 표시
                if ("ko".equalsIgnoreCase(lang) && p.productNameKo() != null && !p.productNameKo().isBlank()) {
                    return p.productNameKo();
                }
                if (p.productName() != null && !p.productName().isBlank()) {
                    return p.productName();
                }
                return "(이름 정보 없음)";
            }).orElse("(제품 정보를 불러오지 못함)");

            String imageUrl = opt.map(p -> p.imageUrl()).orElse(null);

            result.add(new FavoriteItemView(
                    code,
                    name,
                    imageUrl,
                    f.getCreatedAt().format(fmt)
            ));
        }

        return result;
    }

}
