package com.example.foodfacts.service;

import com.example.foodfacts.domain.Comment;
import com.example.foodfacts.domain.UserAccount;
import com.example.foodfacts.repository.CommentRepository;
import com.example.foodfacts.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 비즈니스 로직
 */
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final UserAccountRepository userRepo;

    public CommentService(CommentRepository commentRepo, UserAccountRepository userRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(String productCode) {
        return commentRepo.findByProductCodeOrderByCreatedAtDesc(productCode);
    }

    @Transactional
    public void addComment(String productCode, String username, String content) {
        // 로그인 사용자는 username이 보장된다고 가정(스프링 시큐리티에서)
        UserAccount author = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다."));

        commentRepo.save(new Comment(productCode, author, content));
    }
}
