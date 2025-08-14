package com.back.domain.wiseSaying.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class WiseSaying {
    private int id;
    private String saying;
    private String author;

    public WiseSaying(String saying, String author) {
        this.saying = saying;
        this.author = author;
    }

    public boolean isNew() {
        return id == 0;
    }
}
