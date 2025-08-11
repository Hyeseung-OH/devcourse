package com.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private Scanner sc;
    private int lastId = 0;
    private List<WiseSaying> wiseSayings = new ArrayList<>();

    public WiseSayingController(Scanner sc){
        this.sc = sc;
    }

    // 명언 추가
    public void actionAdd() {
        System.out.print("명언 : ");
        String saying = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        lastId++;

        WiseSaying wiseSaying = new WiseSaying(lastId, saying, author);
        wiseSayings.add(wiseSaying);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));
    }

    // 명언 목록
    public void actionList() {
        System.out.print("번호 / 작가 / 명언");
        System.out.print("------------------");

        wiseSayings
                .reversed()
                .stream()
                .forEach(wiseSaying -> System.out.printf("%d / %s / %s%n",
                        wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getSaying()));
    }
}
