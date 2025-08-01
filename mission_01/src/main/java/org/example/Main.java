package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<WiseSaying> sayinglist = new ArrayList<>();
        int num = 1;

        while(true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            if (command.equals("종료")) {
                break;
            } else if (command.equals("등록")) {
                System.out.print("명언 : ");
                String saying = sc.nextLine();
                System.out.print("작가 : ");
                String author = sc.nextLine();

                // WiseSaying 인스턴스 객체 생성
                WiseSaying ws = new WiseSaying(num, saying, author);
                // sayinglist에 생성한 객체 저장
                sayinglist.add(ws);

                System.out.println("%d번 명언이 등록되었습니다.".formatted((num)));
                num++;
            } else if (command.equals("수정")) {
                System.out.print("id=");
                int chgnum = sc.nextInt();
                sc.nextLine();
                // 해당 번호 파일의 존재 유무 담을 boolean
                boolean found = false;

                for(int i = 0; i < sayinglist.size(); i++) {
                    // 변경할 파일의 번호와 동일한 번호가 있다면
                    if(sayinglist.get(i).id == chgnum) {
                        System.out.println("명언(기존) : " + sayinglist.get(i).saying);
                        System.out.print("명언 : ");
                        String saying = sc.nextLine();

                        System.out.println("작가(기존) : " + sayinglist.get(i).author);
                        System.out.print("작가 : ");
                        String author = sc.nextLine();

                        // 기존에 있던 객체 삭제
                        sayinglist.remove(i);
                        // 새로 변경할 속담을 담은 객체를 저장
                        WiseSaying ws = new WiseSaying(chgnum, saying, author);
                        sayinglist.add(ws);

                        found = true;
                        break;
                    }
                }

                // 변경할 파일의 번호와 동일한 번호가 없다면
                if (!found) {
                    System.out.println(chgnum + "번 명언은 존재하지 않습니다.");
                }
            } else if (command.equals("삭제")) {
                System.out.print("id=");
                int delnum = sc.nextInt();
                sc.nextLine();
                boolean found = false;

                for(int i = 0; i < sayinglist.size(); i++) {
                    // 변경할 파일의 번호와 동일한 번호가 있다면
                    if(sayinglist.get(i).id == delnum) {
                        sayinglist.remove(i);
                        System.out.println(delnum + "번 명언이 삭제되었습니다.");
                        found = true;
                        break;
                    }
                }

                // 변경할 파일의 번호와 동일한 번호가 없다면
                if (!found) {
                    System.out.println(delnum + "번 명언은 존재하지 않습니다.");
                }
            } else if (command.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                for(int i = sayinglist.size() - 1; i >= 0; i--) {
                    WiseSaying ws = sayinglist.get(i);
                    System.out.println("%d / %s / %s".formatted(ws.id, ws.author, ws.saying));
                }
            } else {
                System.out.println("잘못된 명령입니다.");
            }
        }
    }
}

class WiseSaying {
    int id;
    String saying;
    String author;

    public WiseSaying(int id, String saying, String author){
        this.id = id;
        this.saying = saying;
        this.author = author;
    }
}