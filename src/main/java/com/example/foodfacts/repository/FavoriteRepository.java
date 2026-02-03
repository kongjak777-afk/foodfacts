package com.example.foodfacts.repository;

import com.example.foodfacts.domain.Favorite;
import com.example.foodfacts.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndProductCode(UserAccount user, String productCode);

    Optional<Favorite> findByUserAndProductCode(UserAccount user, String productCode);

    List<Favorite> findByUserOrderByCreatedAtDesc(UserAccount user);
}
