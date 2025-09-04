package com.spring3.domain.post.comment.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class CommentWriteForm {
        @NotBlank(message = "댓글 내용을 입력해 주세요")
        @Size(min = 2, max = 100, message = "댓글은 2자 이상 100자 이하로 입력해 주세요")
        private String content;
    }

    @PostMapping("/posts/{postId}/comments/write")
    @Transactional
    // 유효성 체크하는 건 꼭 필수로 넣어야 함
    public String write(
            @PathVariable Long postId,
            @Valid CommentWriteForm form
    ) {
        Post post = postService.findById(postId).get();

        postService.writeComment(post, form.getContent());
        // PostService의 save() 메서드를 호출하지 않아도, 트랜잭션이 커밋될 때 변경 감지(dirty checking)에 의해 자동으로 저장됨
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    @Transactional
    public String delete(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Post post = postService.findById(postId).get();
        postService.delete(post, commentId);

        return "redirect:/posts/" + postId;
    }
}
