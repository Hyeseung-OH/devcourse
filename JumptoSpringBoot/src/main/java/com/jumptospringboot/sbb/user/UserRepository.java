package com.jumptospringboot.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username); // 사용자 ID로 SiteUser 엔티티 조회하는 메서드
}
