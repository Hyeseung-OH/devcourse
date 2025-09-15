package com.jumptospringboot.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// Spring Boot 3.x: spring-boot-starter-security 추가 → 추가 설정 없이는 보안 기능이 활성화되지 않음

@Configuration // 이 파일이 스프링의 환경 설정임을 의미하는 어노테이션
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션 -> 스프링 시큐리티 활성화
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
        ;
        return http.build();
    }
}
