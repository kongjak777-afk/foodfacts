package com.example.foodfacts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security 설정
 *
 * 핵심 규칙:
 * - requestMatchers(...) 들을 먼저 쭉 나열
 * - anyRequest()는 반드시 "마지막"에 둔다 (중요!)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 해시 표준
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // 1) 누구나 접근 허용
                        // =========================

                        // 정적 리소스
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // 회원가입/로그인
                        .requestMatchers("/auth/**", "/login").permitAll()

                        // 홈 + 제품 조회는 누구나
                        .requestMatchers(HttpMethod.GET, "/", "/products/**").permitAll()

                        // =========================
                        // 2) 로그인 필요
                        // =========================

                        // 댓글 작성/삭제/수정 POST들
                        // - /products/{code}/comments
                        // - /products/{code}/comments/{id}/delete
                        // - /products/{code}/comments/{id}/edit (POST)
                        .requestMatchers(HttpMethod.POST, "/products/*/comments/**").authenticated()

                        // 댓글 수정 화면(GET)은 /products/** permitAll에 걸리지만
                        // 서비스에서 본인 검증을 하므로 실질적으로 막힙니다.
                        // 더 엄격히 하고 싶으면 아래를 permitAll 위로 빼서 authenticated로 걸어도 됩니다.
                        // .requestMatchers(HttpMethod.GET, "/products/*/comments/*/edit").authenticated()

                        // 즐겨찾기
                        .requestMatchers(HttpMethod.POST, "/products/*/favorite").authenticated()
                        .requestMatchers(HttpMethod.POST, "/products/*/unfavorite").authenticated()

                        // 마이페이지(즐겨찾기 목록)
                        .requestMatchers("/me/**").authenticated()

                        // =========================
                        // 3) 그 외는 기본적으로 인증 요구 (반드시 마지막!)
                        // =========================
                        .anyRequest().authenticated()
                )

                // 커스텀 로그인 페이지
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
