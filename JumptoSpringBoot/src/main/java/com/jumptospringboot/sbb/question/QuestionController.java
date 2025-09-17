package com.jumptospringboot.sbb.question;

import com.jumptospringboot.sbb.answer.AnswerForm;
import com.jumptospringboot.sbb.user.SiteUser;
import com.jumptospringboot.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

// 프리픽스(prefix): URL의 접두사 또는 시작 부분을 가리키는 말 [필수 X]
// QuestionController에 속하는 URL 매핑은 항상 /question 프리픽스로 시작하므로,
// @RequestMapping("/question") 어노테이션을 사용함
@RequestMapping("/question")
@Controller
// 롬복(Lombok)이 제공하는 어노테이션으로, final이 붙은 속성을 포함하는 생성자를 자동으로 만들어 줌
@RequiredArgsConstructor
public class QuestionController {
    // @RequiredArgsConstructor 어노테이션 사용 결과, 생성자가 자동으로 생성되어 객체가 자동으로 주입됨
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    // 템플릿을 사용하기 때문에 @ResponseBody 어노테이션은 필요없음
    // 매개변수로 사용된 Model 객체는 자바 클래스와 템플릿 간의 연결 고리 역할 => Model 객체에 값을 담아 두면 템플릿에서 값을 사용할 수 있음
    // Model 객체는 따로 생성할 필요 없이, 컨트롤러의 메서드에 매개변수로 지정하기만 하면 스프링 부트가 자동으로 Model 객체 생성
    // @RequestParam(value = "page", defaultValue = "0") -> 스프링 부트의 페이징 기능을 구현할 때 첫 페이지 번호는 0이므로 기본값으론 0을 설정
    // GET 방식에서는 값을 전달하기 위해 ?와 &를 이용하는데, 첫 번째 파라미터는 ? 기호를 사용하고 그 이후 추가되는 값은 & 기호를 사용
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        // Service를 이용해서 repository에 우회 접근 : 컨트롤러 -> 서비스 -> 리포지터리 순서로 접근
        Page<Question> paging = this.questionService.getList(page, kw);
        // Model 객체에 값 추가
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    // 게시글 단건 조회
    @GetMapping(value = "/detail/{id}")
    // @PathVariable 어노테이션을 사용하면 URL 경로에 있는 값을 매개변수로 받을 수 있음
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    // 게시글 생성
    // 생성은 원래 POST여야 하는데 Get으로 요청을 받은 이유 => 템플릿에서 질문 등록하기 버튼을 통한 요청이 GET 요청이므로
    // questionCreate 메서드는 question_form 템플릿을 출력함
    // 매개변수로 바인딩한 객체는 Model 객체로 전달하지 않아도 템플릿에서 사용할 수 있음
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    // 메서드 오버로딩: 한 클래스에서 동일한 메서드명을 사용할 수 있는 것
    // POST 방식으로 요청된 것을 처리
    // @Valid: QuestionForm에서 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작하도록 하는 어노테이션
    // BindingResult: @Valid 어노테이션으로 검증이 수행된 결과를 의미하는 객체
    // RindingResult 객체는 무조건 @Valid 뒤에 위치해야 함, 위치가 뒤바뀌면 검증 실패 시 400 오류 발생
    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 실행됨 => 로그아웃 상태라면 로그인 페이지로 강제 이동
    @PostMapping("/create")
    public String questionCreate(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal) {
        // @Valid 검증 과정이 false면 다시 입력하게 반환
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list"; // 질문 저장 후 질문 목록으로 이동
    }

    // 질문 수정 기능
    // 수정 폼을 보여 주기 위한 요청 -> 수정 버튼을 클릭, GET 요청으로 기존 데이터가 채워진 폼을 보여 줌
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    // 질문 수정 기능 - 오버로딩
    // 실제 수정 데이터를 처리하기 위한 요청 -> 실제 데이터 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal,
                                 @PathVariable("id") Integer id) {
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    // 질문 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal,
                                 @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.questionService.delete(question);

        return "redirect:/";
    }

    // 질문 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
