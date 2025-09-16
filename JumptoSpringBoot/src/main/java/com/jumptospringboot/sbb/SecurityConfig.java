package com.jumptospringboot.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// Spring Boot 3.x: spring-boot-starter-security 추가 → 추가 설정 없이는 보안 기능이 활성화되지 않음

@Configuration // 이 파일이 스프링의 환경 설정임을 의미하는 어노테이션
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션 -> 스프링 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled = true) // 메서드 단위로 보안 설정을 적용할 수 있도록 하는 어노테이션, @PreAuthorize 사용하려면 무조건 설정
public class SecurityConfig {
    // 빈(bean)은 스프링에 의해 생성 또는 관리되는 객체를 의미
    // Bean 어노테이션을 통해 자바 코드 내에서 별도로 빈을 정의하고 등록 가능
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/**").permitAll()) // AntPathRequestMatcher 지원 중지됨 -> 제거

                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                // .formLogin 메서드: 스프링 시큐리티의 로그인 설정을 담당하는 부분
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 로그인 페이지의 URL
                        .defaultSuccessUrl("/")) // 로그인 성공 시 이동할 페이지
                // .logout 메서드: 스프링 시큐리티의 로그아웃 설정을 담당하는 부분
                .logout((logout) -> logout
                        .logoutUrl("/user/logout")  // AntPathRequestMatcher 대신 직접 URL 지정
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)) // 로그아웃 시 생성된 사용자 세션도 삭제
        ;
        return http.build();
    }

    // PasswordEncoder 빈
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // 스프링 시큐리티의 인증을 처리함
    // AuthenticationManager: 사용자 인증 시 UserSecurityService와 PasswordEncoder를 내부적으로 사용하여 인증과 권한 부여 프로세스 처리
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
