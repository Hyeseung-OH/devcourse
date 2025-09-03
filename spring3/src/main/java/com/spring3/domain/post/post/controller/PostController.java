package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @AllArgsConstructor
    @Getter
    public static class PostWriteForm {
        @NotBlank(message = "01-title-제목을 입력해 주세요.")
        @Size(min = 2, max = 10, message = "02-title-제목은 2글자 이상 10글자 이하로 입력해 주세요.")
        private String title;

        @NotBlank(message = "03-content-내용을 입력해 주세요.")
        @Size(min = 2, max = 100, message = "04-content-내용은 2글자 이상 100글자 이하로 입력해 주세요.")
        private String content;
    }

    @GetMapping("/posts/write")
    public String write(@ModelAttribute("form") PostWriteForm form) {
        return "post/write";
    }

    @PostMapping("/posts/write")
    public String doWrite(
            @ModelAttribute("form") @Valid PostWriteForm form, BindingResult bindingResult, Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "post/write";
        }

        Post post = postService.write(form.title, form.content);
        model.addAttribute("id", post.getId());
        return "redirect:/posts/%d".formatted(post.getId()); // 주소창을 바꿔
    }


    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostModifyForm {
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 10, message = "02-title-제목은 2글자 이상 10글자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 100, message = "04-content-내용은 2글자 이상 100글자 이하로 입력해주세요.")
        private String content;
    }

    @GetMapping("/posts/{id}/modify")
    public String modify(
            @PathVariable Long id,
            @ModelAttribute("form") PostModifyForm form,
            Model model
    ) {
        Post post = postService.findById(id).get();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        model.addAttribute("post", post);
        return "post/modify";
    }

    @PostMapping("/posts/{id}/modify")
    @Transactional
    public String doModify(
            @PathVariable Long id,
            @ModelAttribute("form") @Valid PostModifyForm form,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "post/modify";
        }

        Post post = postService.findById(id).get();
        postService.modify(post, form.title, form.content);

        return "redirect:/posts/%d".formatted(post.getId());
    }


    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        return "post/detail";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/list";
    }


}