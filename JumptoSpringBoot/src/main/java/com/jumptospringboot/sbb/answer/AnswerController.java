package com.jumptospringboot.sbb.answer;

import com.jumptospringboot.sbb.question.Question;
import com.jumptospringboot.sbb.question.QuestionService;
import com.jumptospringboot.sbb.user.SiteUser;
import com.jumptospringboot.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

// 프리픽스
@RequestMapping("/answer")
// 생성자 자동 주입
@RequiredArgsConstructor
// 컨트롤러임을 명시
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    // 답변을 저장함
    // @PostMapping(value="/create/{id}")에서 value는 생략해도 됨
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    // @RequestParam: 사용자가 입력한 값을 전달받기 위해 추가한 어노테이션
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult,
                               Principal principle) { // principal.getName()을 호출하면 현재 로그인한 사용자의 사용자명을 알 수 있음
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principle.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
