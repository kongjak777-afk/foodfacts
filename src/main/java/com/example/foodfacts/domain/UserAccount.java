package com.example.foodfacts.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 최소 가입 정보만 저장하는 사용자 엔티티
 * - username: 로그인 ID 겸 닉네임 (이메일/전화번호 저장 안 함)
 * - passwordHash: BCrypt로 해시된 비밀번호만 저장 (원문 비번 저장 금지)
 */
@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저가 쓰는 닉네임/ID (중복 방지)
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    // BCrypt 해시 저장
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected UserAccount() { }

    public UserAccount(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- getter ----
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
