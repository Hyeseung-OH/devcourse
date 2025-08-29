package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WiseSayingService {
    private List<WiseSaying> wiseSayingList = new ArrayList<>() {{
        add(new WiseSaying(1, "명언1", "작가1"));
        add(new WiseSaying(2, "명언2", "작가2"));
        add(new WiseSaying(3, "명언3", "작가3"));
        add(new WiseSaying(4, "명언4", "작가4"));
        add(new WiseSaying(5, "명언5", "작가5"));
        add(new WiseSaying(6, "명언6", "작가6"));
        add(new WiseSaying(7, "명언7", "작가7"));
        add(new WiseSaying(8, "명언8", "작가8"));
    }};

    private int lastId = 8;

    public WiseSaying write(String content, String author) {
        if(content == null || content.trim().length() == 0) {
            throw new RuntimeException("명언을 입력해 주세요.");
        }

        if(author == null || author.trim().length() == 0) {
            throw new RuntimeException("작가를 입력해 주세요.");
        }

        WiseSaying wiseSaying = new WiseSaying(++lastId, content, author);
        wiseSayingList.add(wiseSaying);

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        return wiseSayingList;
    }

    public WiseSaying findById(int id) {
        Optional<WiseSaying> wiseSaying = wiseSayingList.stream()
                .filter(w -> w.getId() == id)
                .findFirst();

        if(wiseSaying.isEmpty()) {
            throw new RuntimeException("%d번 명언은 존재하지 않습니다.".formatted(id));
        }

        return wiseSaying.get();
    }

    public void delete(WiseSaying wiseSaying) {
        wiseSayingList.remove(wiseSaying);
    }

    public void modify(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.update(content, author);
    }
}
