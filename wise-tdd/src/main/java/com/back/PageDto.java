package com.back;

import com.back.domain.wiseSaying.entity.WiseSaying;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageDto {
    // DTO => 데이터를 묶는 객체
    private int page; // 현재 페이지
    private int pageSize; // 페이지당 아이템 수
    private int totalItems; // 전체 아이템 수
    private List<WiseSaying> content; // 실제 데이터
}
