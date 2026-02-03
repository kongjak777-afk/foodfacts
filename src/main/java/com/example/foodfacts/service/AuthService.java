package com.example.foodfacts.service;

import com.example.foodfacts.domain.UserAccount;
import com.example.foodfacts.dto.RegisterRequestDto;
import com.example.foodfacts.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 가입 로직 담당
 * - username 중복 체크
 * - password BCrypt 해시 후 저장
 */
@Service
public class AuthService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequestDto req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // BCrypt로 해시(원문 저장 금지)
        String hash = passwordEncoder.encode(req.password());

        userRepo.save(new UserAccount(req.username(), hash));
    }
}
