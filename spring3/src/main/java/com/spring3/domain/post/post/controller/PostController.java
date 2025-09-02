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

    private String getWriteFormHtml(String errorMessage, String title, String content) {
        return """
                <ul style="color:red">%s</ul>
                
                <form method="POST" action="/posts/doWrite">
                  <input type="text" name="title" value="%s" autoFocus>
                  <br>
                  <textarea name="content">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                
                <script>
                    const li = document.querySelector("ul li");
                    const errorFieldName = li.dataset.errorFieldName;
                
                    if(errorFieldName.length > 0) {
                        const form = document.querySelector("form");
                        form[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content);
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
        return getWriteFormHtml("", "", "");
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            @Valid PostWriteForm form, BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            // Stream 이용
            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(field -> field.getField() + "-" + field.getDefaultMessage())
                    .map(message -> message.split("-"))
                    .map(bits -> """
                            <!-- %s --><li data-error-field-name="%s">%s</li>
                            """.formatted(bits[1], bits[0], bits[2]))
                    .sorted()
                    .collect(Collectors.joining("\n"));

            return getWriteFormHtml(errorMessages, form.title, form.content);
        }

        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}