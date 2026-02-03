package com.example.foodfacts.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 댓글 엔티티
 * - productCode: Open Food Facts 바코드(code)와 연결
 * - author: UserAccount (작성자)
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제품 바코드 (예: 3017620422003)
    @Column(nullable = false, length = 40)
    private String productCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAccount author;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Comment() { }

    public Comment(String productCode, UserAccount author, String content) {
        this.productCode = productCode;
        this.author = author;
        this.content = content;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- getter ----
    public Long getId() { return id; }
    public String getProductCode() { return productCode; }
    public UserAccount getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
