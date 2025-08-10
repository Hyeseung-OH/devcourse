package com.back;

import java.util.Scanner;

public class App {

    private Scanner sc = new Scanner(System.in);
    private int lastNo = 0;
    private WiseSaying[] wiseSayings = new WiseSaying[100];
    private int lastIndex = 0;

    public void run() {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            if (command.equals("등록")) {
                actionWrite();

            } else if (command.equals("목록")) {
                actionList();

            } else if (command.startsWith("삭제")) {
                actionDelete(command);

            } else if (command.startsWith("수정")) {
                actionModify(command);

            } else if (command.equals("종료")) {
                break;
            }
        }
    }

    private void actionModify(String command) {

        String[] commandBits = command.split("=");

        if (commandBits.length < 2) {
            System.out.println("번호를 입력해주세요.");
            return;
        }

        String idStr = commandBits[1];
        int id = Integer.parseInt(idStr);

        int modifyTargetIndex = findIndexById(id);

        if(modifyTargetIndex == -1) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        WiseSaying modifyTargetWiseSaying = wiseSayings[modifyTargetIndex];

        System.out.println("명언(기존) : %s".formatted(modifyTargetWiseSaying.getSaying()));
        System.out.print("명언 : ");
        String newSaying = sc.nextLine();
        System.out.println("작가(기존) : %s".formatted(modifyTargetWiseSaying.getAuthor()));
        System.out.print("작가 : ");
        String newAuthor = sc.nextLine();

        modify(modifyTargetWiseSaying, newSaying, newAuthor);
    }

    private void modify(WiseSaying modifyTargetWiseSaying, String newSaying, String newAuthor) {
        modifyTargetWiseSaying.setSaying(newSaying);
        modifyTargetWiseSaying.setAuthor(newAuthor);
    }

    private void actionDelete(String command) {

        String[] commandBits = command.split("=");

        if (commandBits.length < 2) {
            System.out.println("번호를 입력해주세요.");
            return;
        }

        String idStr = commandBits[1];
        int id = Integer.parseInt(idStr);

        boolean result = delete(id);

        if (result) {
            System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
        } else {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
        }
    }

    private int findIndexById(int id) {
        for (int i = 0; i < lastIndex; i++) {
            if (wiseSayings[i].getId() == id) {
                return i;
            }
        }

        return -1;
    }

    private boolean delete(int id) {

        int deleteTargetIndex = findIndexById(id);

        if (deleteTargetIndex == -1) {
            return false;
        }

        for (int i = deleteTargetIndex; i < lastIndex; i++) {
            wiseSayings[i] = wiseSayings[i + 1];
        }

        lastIndex--;

        return true;
    }

    private void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        WiseSaying[] wiseSayings = findListDesc();

        for (WiseSaying wiseSaying : wiseSayings) {
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getSaying(), wiseSaying.getAuthor()));
        }
    }

    private WiseSaying[] findListDesc() {

        WiseSaying[] resultList = new WiseSaying[lastIndex];
        int resultListIndex = 0;

        for (int i = lastIndex - 1; i >= 0; i--) {
            resultList[resultListIndex] = wiseSayings[i];
            resultListIndex++;
        }

        return resultList;
    }

    private void actionWrite() {
        System.out.print("명언 : ");
        String saying = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = write(saying, author);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    private WiseSaying write(String saying, String author) {

        lastNo++;
        WiseSaying wiseSaying = new WiseSaying(lastNo, saying, author);

        wiseSayings[lastIndex++] = wiseSaying;

        return wiseSaying;
    }
}
