package day1.exam21;

public class Main {
    public static void main(String[] args) {
        Object obj = "Hello";

        if (obj instanceof String) {
            String str = (String) obj; // 명시적 캐스팅 필요
            System.out.println(str.length());
        }
    }
}