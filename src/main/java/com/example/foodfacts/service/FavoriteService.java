package com.example.foodfacts.service;

import com.example.foodfacts.domain.Favorite;
import com.example.foodfacts.domain.UserAccount;
import com.example.foodfacts.repository.FavoriteRepository;
import com.example.foodfacts.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final UserAccountRepository userRepo;

    public FavoriteService(FavoriteRepository favoriteRepo, UserAccountRepository userRepo) {
        this.favoriteRepo = favoriteRepo;
        this.userRepo = userRepo;
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
}
