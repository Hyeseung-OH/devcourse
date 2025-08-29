package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WiseSayingController {

    private List<WiseSaying> wiseSayingList = new ArrayList<>();
    private int lastId = 0;

    @GetMapping("/wiseSaying/write")
    @ResponseBody
    public String write(
            String content,
            String author
    ) {

        if(content == null || content.trim().length() == 0) {
            throw new RuntimeException("명언을 입력해주세요.");
        }

        if(author == null || author.trim().length() == 0) {
            throw new RuntimeException("작가를 입력해주세요.");
        }

        WiseSaying wiseSaying = new WiseSaying(++lastId, content, author);
        wiseSayingList.add(wiseSaying);

        return "%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId());
    }

    @GetMapping("/wiseSaying/list")
    @ResponseBody
    public String list() {

        String wiseSayings = wiseSayingList.stream()
                .map(w -> "<li>%s / %s / %s</li>".formatted(w.getId(), w.getContent(), w.getAuthor()))
                .collect(Collectors.joining("\n"));

        return """
                <ul>
                %s
                </ul>
                """.formatted(wiseSayings);
    }
}