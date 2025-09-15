package com.jumptospringboot.sbb.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// 스프링 시큐리티에 이미 User 클래스가 있기 때문에 SiteUser로 만듦
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique = true는 값을 중복되게 저장할 수 없음을 뜻함
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}
