package com.back.domain.post.entity;

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

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
