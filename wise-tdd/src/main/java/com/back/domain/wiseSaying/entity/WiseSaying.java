package com.back.domain.wiseSaying.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class WiseSaying {
    private int id;
    private String saying;
    private String author;

    public WiseSaying(String saying, String author) {
        this.saying = saying;
        this.author = author;
    }

    public WiseSaying(Map<String, Object> map) {
        this.id = (Integer) map.get("id");
        this.saying = (String) map.get("saying");
        this.author = (String) map.get("author");
    }

    public boolean isNew() {
        return id == 0;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("id", id);
        map.put("saying", saying);
        map.put("author", author);

        return map;
    }
}
