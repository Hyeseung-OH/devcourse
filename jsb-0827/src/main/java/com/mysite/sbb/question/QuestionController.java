package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionRepository questionRepository;

    @GetMapping("/question/list")
    public String list(Model model) {
//        String questions = questionRepository.findAll()
//                .stream()
//                .map(e -> "<li>%s</li>".formatted(e.getSubject()))
//                .collect(Collectors.joining("\n"));

//        String value = "홍길동";
//        List<Integer> list = List.of(1,2, 3, 4,5);
//
//        model.addAttribute("value", value);
//        model.addAttribute("list", list);

        List<Question> questionList = questionRepository.findAll();
        model.addAttribute("questionList", questionList);

        return "question_list";
    }
}