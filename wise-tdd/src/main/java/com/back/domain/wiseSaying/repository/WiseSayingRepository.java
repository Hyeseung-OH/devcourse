package com.back.domain.wiseSaying.repository;

import com.back.PageDto;
import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.Optional;

public interface WiseSayingRepository {
    WiseSaying save(WiseSaying wiseSaying);
    boolean delete(WiseSaying wiseSaying);
    Optional<WiseSaying> findById(int id);
    PageDto findByContentContainingDesc(String kw, int pageSize, int pageNo);
    PageDto findByAuthorContainingDesc(String kw, int pageSize, int pageNo);
    PageDto findByContentContainingOrAuthorContainingDesc(String kw, int pageSize, int pageNo);
}