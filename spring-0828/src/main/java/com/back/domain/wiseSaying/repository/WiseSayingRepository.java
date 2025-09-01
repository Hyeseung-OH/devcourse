package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WiseSayingRepository extends JpaRepository<WiseSaying, Integer> {
}