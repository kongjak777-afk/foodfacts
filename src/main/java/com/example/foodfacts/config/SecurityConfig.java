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
 * 아주 단순한 폼 로그인 기반 보안 설정
 * - 조회: 누구나 가능
 * - 댓글 작성(POST): 로그인 필요
 *
 * 주의: 연습 프로젝트이므로 최소 설정만 적용합니다.
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
                // CSRF는 기본 ON. (타임리프 폼에 토큰 넣으면 문제 없음)
                .csrf(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스가 있다면 허용 (추후 css/js 쓰면 필요)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // 회원가입/로그인 페이지는 누구나 접근 가능
                        .requestMatchers("/auth/**", "/login").permitAll()

                        // 제품/홈 조회는 누구나 가능
                        .requestMatchers(HttpMethod.GET, "/", "/products/**").permitAll()

                        // 댓글 작성(POST)은 로그인 필요
                        .requestMatchers(HttpMethod.POST, "/products/**/comments").authenticated()

                        // 나머지는 기본적으로 인증 필요(보수적으로)
                        .anyRequest().authenticated()
                )

                // 기본 폼 로그인 사용 (우리가 login.html 만들 예정)
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
