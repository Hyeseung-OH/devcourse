package com.back.domain.post.post.entity;

import com.back.domain.post.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends BaseEntity  {
    private String title;
    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne
    private Member author;
    public Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
