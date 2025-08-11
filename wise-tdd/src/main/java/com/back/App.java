package com.back;

import java.util.Scanner;

public class App {
    private Scanner sc;
    private WiseSayingController wiseSayingController = new WiseSayingController(sc);
    private SystemController systemController = new SystemController();

    public App(Scanner sc){
        this.sc = sc;
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while(true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            switch (cmd) {
                case "등록" -> wiseSayingController.actionAdd();
                case "목록" -> wiseSayingController.actionList();
                case "종료" -> {
                    systemController.actionExit();
                    return;
                }
            }
        }
    }



}
