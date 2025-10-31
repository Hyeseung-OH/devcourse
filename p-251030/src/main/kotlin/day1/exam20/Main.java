package day1.exam20;

public class Main {
    public static void main(String[] args) {
        String name = null;
        String displayName = (name != null) ? name : "Guest";

        System.out.println("Hello, " + displayName);
    }
}