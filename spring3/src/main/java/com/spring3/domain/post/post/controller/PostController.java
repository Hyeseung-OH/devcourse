package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private String getWriteFormHtml(String errorMessage, String title, String content, String errorFieldName) {
        return """
                <div style="color:red">%s</div>
                
                <form method="POST" action="/posts/doWrite">
                  <input type="text" name="title" value="%s" autoFocus>
                  <br>
                  <textarea name="content">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                
                <script>
                    const errorFieldName = "%s";
                
                    if(errorFieldName.length > 0) {
                        const form = document.querySelector("form");
                        form[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content, errorFieldName);
    }

    @AllArgsConstructor
    @Getter
    public static class PostWriteForm {
        @NotBlank(message = "1-제목을 입력해 주세요.")
        @Size(min = 2, max = 10, message = "2-제목은 2글자 이상 10글자 이하로 입력해 주세요.")
        private String title;

        @NotBlank(message = "3-내용을 입력해 주세요.")
        @Size(min = 2, max = 100, message = "4-내용은 2글자 이상 100글자 이하로 입력해 주세요.")
        private String content;
    }


    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return getWriteFormHtml("", "", "", "");
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            @Valid PostWriteForm form, BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            String fieldName = "title";

            // 명령형
//            List<FieldError> fieldErrorsList = bindingResult.getFieldErrors();
//
//            StringBuilder sb = new StringBuilder("");
//
//            for(FieldError fieldError : fieldErrorsList) {
//               sb.append(fieldError.getDefaultMessage());
//                sb.append("<br>");
//            }
//
//            String errorMessages = sb.toString();

            // Stream 이용
            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .sorted()
                    .collect(Collectors.joining("<br>"));

            return getWriteFormHtml(errorMessages, form.title, form.content, fieldName);
        }

//        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요.", title, content, "title");
//        if(title.length() < 2) return getWriteFormHtml("제목을 2자 이상 입력해주세요.", title, content, "title");
//        if(title.length() > 10) return getWriteFormHtml("제목을 10자 이하로 입력해주세요.", title, content, "title");
//        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요.", title, content, "content");
//        if(content.length() < 2) return getWriteFormHtml("내용을 2자 이상 입력해주세요.", title, content, "content");
//        if(content.length() > 1000) return getWriteFormHtml("내용을 1000자 이하로 입력해주세요.", title, content, "content");

        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}