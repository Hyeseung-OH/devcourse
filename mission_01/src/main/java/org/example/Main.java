package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.print("명령) ");
            String prompt = sc.nextLine();

            if (prompt.equals("종료")) {
                break;
            } else if (prompt.equals("등록")) {
                System.out.print("명언 : ");
                String saying = sc.nextLine();
                System.out.print("작가 : ");
                String author = sc.nextLine();

            } else if (prompt.equals("수정")) {

            } else if (prompt.equals("삭제")) {

            } else if (prompt.equals("목록")) {

            } else {
                System.out.println("잘못된 명령입니다.");
            }
        }
    }
}