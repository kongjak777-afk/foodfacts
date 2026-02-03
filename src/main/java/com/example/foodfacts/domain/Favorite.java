package com.example.foodfacts.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 즐겨찾기 엔티티
 * - user 1명 + productCode 1개 조합은 유일해야 함(중복 즐겨찾기 방지)
 */
@Entity
@Table(
        name = "favorite",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_favorite_user_product", columnNames = {"user_id", "product_code"})
        }
)
public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    // 제품 바코드
    @Column(name = "product_code", nullable = false, length = 40)
    private String productCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Favorite() {}

    public Favorite(UserAccount user, String productCode) {
        this.user = user;
        this.productCode = productCode;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public UserAccount getUser() { return user; }
    public String getProductCode() { return productCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
