package com.spring3.domain.post.comment.entity;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    private String content;

    // 연관관게 설정
    // Many = Comment, One = Post
    // Post 1개에 Comment 여러개가 달릴 수 있다.
    // FK는 Comment 테이블에 post_id로 생성된다.
    @ManyToOne
    private Post post;
}
