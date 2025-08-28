package com.back.domain.wiseSaying.repository;

import com.back.PageDto;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.util.Util;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WiseSayingRepository {
    WiseSaying save(WiseSaying wiseSaying);
    boolean delete(WiseSaying wiseSaying);
    Optional<WiseSaying> findById(int id);
    PageDto findByContentContainingDesc(String kw, int pageSize, int pageNo);
    PageDto findByAuthorContainingDesc(String kw, int pageSize, int pageNo);
    PageDto findByContentContainingOrAuthorContainingDesc(String kw, int pageSize, int pageNo);
    List<WiseSaying> findAll();

    default String build() {
        List<WiseSaying> wiseSayings = findAll();
        List<Map<String, Object>> mapList = wiseSayings.stream()
                .map(WiseSaying::toMap).toList();

        String jsonStr = Util.json.toString(mapList);
        String filePath = "db/wiseSaying/data.json";

        Util.file.set(filePath, jsonStr);

        return filePath;
    }
}