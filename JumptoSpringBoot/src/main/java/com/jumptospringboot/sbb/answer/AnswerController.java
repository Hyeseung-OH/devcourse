package com.jumptospringboot.sbb.answer;

import com.jumptospringboot.sbb.question.Question;
import com.jumptospringboot.sbb.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 프리픽스
@RequestMapping("/answer")
// 생성자 자동 주입
@RequiredArgsConstructor
// 컨트롤러임을 명시
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    // 답변을 저장함
    // @PostMapping(value="/create/{id}")에서 value는 생략해도 됨
    @PostMapping("/create/{id}")
    // @RequestParam: 사용자가 입력한 값을 전달받기 위해 추가한 어노테이션
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
        Question question = this.questionService.getQuestion(id);
        this.answerService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);
    }
}
