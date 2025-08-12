package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    // 보통 DB
    private List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;


    public Object save(WiseSaying wiseSaying) {
        if(wiseSaying.isNew()) {
            wiseSaying.setId(++lastId);
            wiseSayings.add(wiseSaying);
        }

        return wiseSaying;
    }

    public boolean delete(int id) {
        return wiseSayings.removeIf(wiseSaying -> wiseSaying.getId() == id);
    }

    public WiseSaying findByIdOrNull(int id) {
        return wiseSayings.stream()
                .filter(wiseSaying -> wiseSaying.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<WiseSaying> findByContentContainingDesc(String kw, int pageSize, int pageNo) {
        return wiseSayings.reversed().stream()
                .filter(w -> w.getSaying().contains(kw))
                .limit(pageSize)
                .toList();
    }

    public List<WiseSaying> findByAuthorContainingDesc(String kw, int pageSize, int pageNo) {
        return wiseSayings.reversed().stream()
                .filter(w -> w.getAuthor().contains(kw))
                .limit(pageSize)
                .toList()
                .reversed();
    }

    public List<WiseSaying> findByContentContainingOrAuthorContainingDesc(String kw, int pageSize, int pageNo) {
        return wiseSayings.reversed().stream()
                .filter(w -> w.getAuthor().contains(kw) || w.getSaying().contains(kw))
                .limit(pageSize)
                .toList()
                .reversed();
    }
}
