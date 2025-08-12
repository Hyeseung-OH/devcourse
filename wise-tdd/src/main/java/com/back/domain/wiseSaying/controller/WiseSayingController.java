package com.back.domain.wiseSaying.controller;

import com.back.AppContext;
import com.back.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private WiseSayingService wiseSayingService;
    private Scanner sc;

    public WiseSayingController() {
        this.sc = AppContext.sc;
        this.wiseSayingService = AppContext.wiseSayingService;
    }

    public void actionAdd() {
        System.out.print("명언 : ");
        String saying = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = wiseSayingService.write(saying, author);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayings = wiseSayingService.findListDesc();

        wiseSayings
                .reversed()
                .stream()
                .forEach(wiseSaying -> System.out.printf("%d / %s / %s%n",
                        wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getSaying()));

    }

    public void actionDelete(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        boolean deleted = wiseSayingService.delete(id);

        if(!deleted) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }
}