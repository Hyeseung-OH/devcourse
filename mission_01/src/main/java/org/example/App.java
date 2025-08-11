package org.example;

import controller.SystemController;
import controller.WiseSayingController;
import java.util.Scanner;

public class App {

    private Scanner sc = new Scanner(System.in);

    private SystemController systemController = new SystemController();
    private WiseSayingController wiseSayingController = new WiseSayingController(sc);

    public void run() {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            Rq rq = new Rq(command);
            String actionName = rq.getActionName();
            if (actionName.equals("등록")) {
                wiseSayingController.actionWrite();

            } else if (actionName.equals("목록")) {
                wiseSayingController.actionList();

            } else if (actionName.startsWith("삭제")) {
                wiseSayingController.actionDelete(rq);

            } else if (actionName.startsWith("수정")) {
                wiseSayingController.actionModify(rq);

            } else if (actionName.equals("종료")) {
                systemController.exit();
                // 종료 시 설정 저장
                // 종료 시 데이터 백업
                // 종료 시 자원 해제..

                break;
            }
        }
    }
}