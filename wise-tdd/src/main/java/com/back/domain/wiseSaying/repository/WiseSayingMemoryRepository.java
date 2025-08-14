package com.back.domain.wiseSaying.repository;

import com.back.PageDto;
import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WiseSayingMemoryRepository implements WiseSayingRepository {
    private List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSaying save(WiseSaying wiseSaying) {
        if (wiseSaying.isNew()) {
            wiseSaying.setId(++lastId);
            wiseSayings.add(wiseSaying);
        }

        return wiseSaying;
    }

    public boolean delete(WiseSaying wiseSaying) {
        return wiseSayings.removeIf(w -> w.getId() == wiseSaying.getId());
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayings.stream()
                .filter(wiseSaying -> wiseSaying.getId() == id)
                .findFirst();
    }

    public PageDto findByContentContainingDesc(String kw, int pageSize, int pageNo) {
        List<WiseSaying> filteredContent = wiseSayings.reversed().stream()
                .filter(w -> w.getSaying().contains(kw))
                .toList();

        return pageOf(filteredContent, pageNo, pageSize);
    }

    public PageDto findByAuthorContainingDesc(String kw, int pageSize, int pageNo) {
        List<WiseSaying> filteredContent = wiseSayings.reversed().stream()
                .filter(w -> w.getAuthor().contains(kw))
                .toList();

        return pageOf(filteredContent, pageNo, pageSize);
    }

    public PageDto findByContentContainingOrAuthorContainingDesc(String kw, int pageSize, int pageNo) {
        List<WiseSaying> filteredContent = wiseSayings.reversed().stream()
                .filter(w -> w.getAuthor().contains(kw) || w.getSaying().contains(kw))
                .toList();

        return pageOf(filteredContent, pageNo, pageSize);
    }

    private PageDto pageOf(List<WiseSaying> filteredContent, int pageNo, int pageSize) {
        List<WiseSaying> content = filteredContent.stream()
                .skip((pageNo-1) * pageSize)
                .limit(pageSize)
                .toList();

        int totalItems = filteredContent.size();
        return new PageDto(pageNo, pageSize, totalItems, content);
    }

}