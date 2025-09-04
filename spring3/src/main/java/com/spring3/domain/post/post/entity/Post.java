package com.spring3.domain.post.post.entity;

import com.spring3.domain.post.comment.entity.Comment;
import com.spring3.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Post extends BaseEntity {
    private String title;
    private String content;

    // Cascade 속성은 트랜잭션 안에서만 작동함
    // CascadeType.PERSIST: Post가 저장될 때 연관된 Comment들도 함께 저장
    // CascadeType.REMOVE: Post가 삭제될 때 연관된 Comment들도 함께 삭제
    // fetch = FetchType.LAZY: Post를 조회할 때 연관된 Comment들을 즉시 로드하지 않고, 실제로 접근할 때 로드
    // mappedBy = "post": Comment 엔티티의 post 필드에 의해 매핑됨을 나타냄
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY) // Comment.post 필드에 의해 매핑됨
    // 비어 있는 값을 넣어 주지 않으면 NullPointerException이 발생할 수 있음
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Comment addComment(String content) {
        Comment comment = new Comment(content, this);
        this.comments.add(comment);

        return comment;
    }

    public void deleteComment(Long commentId) {
        comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .ifPresent(comments::remove);
    }
}
