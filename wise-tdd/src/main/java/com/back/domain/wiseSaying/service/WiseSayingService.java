package com.back.domain.wiseSaying.service;

import com.back.AppContext;
import com.back.PageDto;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.Optional;

public class WiseSayingService {
    // 비즈니스에 관련된 것만
    private WiseSayingRepository wiseSayingRepository;

    public WiseSayingService() {
        this.wiseSayingRepository = AppContext.wiseSayingRepository;
    }

    // 용어 선정이 중요함
    public WiseSaying write(String saying, String author) {
        WiseSaying wiseSaying = new WiseSaying(saying, author);
        wiseSayingRepository.save(wiseSaying);

        return wiseSaying;
    }

    public PageDto findListDesc(String kw, String kwdType, int pageSize, int pageNo) {
        return switch(kwdType) {
            case "content" -> wiseSayingRepository.findByContentContainingDesc(kw, pageSize, pageNo);
            case "author" -> wiseSayingRepository.findByAuthorContainingDesc(kw, pageSize, pageNo);
            default -> wiseSayingRepository.findByContentContainingOrAuthorContainingDesc(kw, pageSize, pageNo);
        };
    }


    public boolean delete(int id) {

        Optional<WiseSaying> wiseSayingOpt = wiseSayingRepository.findById(id);
        if (wiseSayingOpt.isEmpty()) {
            return false;
        }

        wiseSayingRepository.delete(wiseSayingOpt.get());
        return true;
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    public void modify(WiseSaying wiseSaying, String newSaying, String newAuthor) {
        wiseSaying.setSaying(newSaying);
        wiseSaying.setAuthor(newAuthor);

        wiseSayingRepository.save(wiseSaying);
    }
}
