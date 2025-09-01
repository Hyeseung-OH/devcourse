package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class WiseSayingMemRepository {
    private List<WiseSaying> wiseSayingList = new ArrayList<>();

    private int lastId = 0;

    public WiseSaying save(WiseSaying wiseSaying) {

        if(wiseSaying.isNew()) {
            wiseSaying.setId(++lastId);
        }
        wiseSayingList.add(wiseSaying);

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        return wiseSayingList;
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayingList.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
    }

    public void delete(WiseSaying wiseSaying) {
        wiseSayingList.remove(wiseSaying);
    }

    public long count() {
        return wiseSayingList.size();
    }
}
