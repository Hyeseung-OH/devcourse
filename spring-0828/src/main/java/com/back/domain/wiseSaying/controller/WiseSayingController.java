package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;
import com.back.standard.MarkdownService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WiseSayingController {
    private final WiseSayingService wiseSayingService;
    private final MarkdownService markdownService;

    @GetMapping("/wiseSaying/write")
    @ResponseBody
    public String write(String content, String author) {
        if(content == null || content.trim().length() == 0) {
            throw new RuntimeException("명언을 입력해주세요.");
        }

        if(author == null || author.trim().length() == 0) {
            throw new RuntimeException("작가를 입력해주세요.");
        }

        WiseSaying wiseSaying = wiseSayingService.write(content, author);
        return "%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId());
    }

    @GetMapping("/wiseSayings")
    @ResponseBody
    public String list() {
        String wiseSayings =  wiseSayingService.findAll().stream()
                .map(w -> "<li>%s / %s / %s</li>".formatted(w.getId(), w.getContent(), w.getAuthor()))
                .collect(Collectors.joining("\n"));

        return """
                <ul>
                %s
                </ul>
                """.formatted(wiseSayings);
    }

    @GetMapping("/wiseSayings/{id}")
    @ResponseBody
    public String detail(@PathVariable int id) {
        WiseSaying wiseSaying = wiseSayingService.findById(id);
        String html = markdownService.toHtml(wiseSaying.getContent());

        return """
                <h1> 번호 : %s </h1>
                <div> 작가 : %s </div>
                <div>명언 : %s</div>
                """.formatted(wiseSaying.getId(),  wiseSaying.getAuthor(), html);    }

    @GetMapping("/wiseSayings/delete/{id}")
    @ResponseBody
    @Transactional
    public String delete(@PathVariable int id) {
        WiseSaying wiseSaying = wiseSayingService.findById(id);
        wiseSayingService.delete(wiseSaying);

        return "%d번 명언이 삭제되었습니다".formatted(id);
    }

    @GetMapping("/wiseSayings/modify/{id}")
    @ResponseBody
    public String modify(@PathVariable int id, @RequestParam(defaultValue = "기본값") String content, @RequestParam(defaultValue = "기본값") String author) {
        WiseSaying wiseSaying = wiseSayingService.findById(id);
        wiseSayingService.modify(wiseSaying, content, author);

        return "%d번 명언이 수정되었습니다.".formatted(wiseSaying.getId());
    }
}