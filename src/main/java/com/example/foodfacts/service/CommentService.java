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

    /**
     * 댓글 삭제 (본인만 가능)
     *
     * @param productCode 제품 코드(바코드) - URL에 포함되어 넘어오므로 검증에 활용 가능
     * @param commentId   삭제할 댓글 ID
     * @param username    현재 로그인한 사용자명
     */
    @Transactional
    public void deleteComment(String productCode, Long commentId, String username) {

        // 1) 댓글 존재 확인
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 2) 해당 제품 댓글이 맞는지 확인(안전장치)
        if (!comment.getProductCode().equals(productCode)) {
            throw new IllegalArgumentException("잘못된 접근입니다(제품 코드 불일치).");
        }

        // 3) 작성자 본인인지 확인
        String authorUsername = comment.getAuthor().getUsername();
        if (!authorUsername.equals(username)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }

        // 4) 삭제
        commentRepo.delete(comment);
    }
    // ===========================
    // ===== [추가] 시작: 수정 =====
    // ===========================
    /**
     * 댓글 수정 (본인만 가능)
     */
    @Transactional
    public void updateComment(String productCode, Long commentId, String username, String newContent) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 안전장치: URL의 제품코드와 댓글의 제품코드가 일치해야 함
        if (!comment.getProductCode().equals(productCode)) {
            throw new IllegalArgumentException("잘못된 접근입니다(제품 코드 불일치).");
        }

        // 본인 검증
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인 댓글만 수정할 수 있습니다.");
        }

        // 내용 변경 (엔티티에 setter를 두지 않고, 전용 메서드로 변경하는 패턴)
        comment.changeContent(newContent);
    }
    @Transactional(readOnly = true)
    public Comment getCommentForEdit(String productCode, Long commentId, String username) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getProductCode().equals(productCode)) {
            throw new IllegalArgumentException("잘못된 접근입니다(제품 코드 불일치).");
        }

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인 댓글만 수정할 수 있습니다.");
        }

        return comment;
    }



}
