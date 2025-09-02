package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Validated
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

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return getWriteFormHtml("", "", "", "");
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            @NotBlank @Size(min=2, max=10) String title,
            @NotBlank @Size(min=2, max=100) String content
    ) {
//        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요.", title, content, "title");
//        if(title.length() < 2) return getWriteFormHtml("제목을 2자 이상 입력해주세요.", title, content, "title");
//        if(title.length() > 10) return getWriteFormHtml("제목을 10자 이하로 입력해주세요.", title, content, "title");
//        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요.", title, content, "content");
//        if(content.length() < 2) return getWriteFormHtml("내용을 2자 이상 입력해주세요.", title, content, "content");
//        if(content.length() > 1000) return getWriteFormHtml("내용을 1000자 이하로 입력해주세요.", title, content, "content");

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}