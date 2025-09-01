package com.back.domain.wiseSaying.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class WiseSaying {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String content;
    private String author;

    public WiseSaying(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public void update(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public boolean isNew() {
        return this.id == 0;
    }
}