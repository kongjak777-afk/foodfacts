package com.example.foodfacts.repository;

import com.example.foodfacts.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 제품에 달린 댓글을 최신순으로 조회
    List<Comment> findByProductCodeOrderByCreatedAtDesc(String productCode);
}
