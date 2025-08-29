package com.back.domain.wiseSaying.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WiseSaying {
    private int id;
    private String content;
    private String author;

    public void update(String content, String author) {
        this.content = content;
        this.author = author;
    }
}