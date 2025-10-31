package day1.exam17;

public class Main2 {
    public static void main(String[] args) {
        int day = 3;

        String dayName = switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            default -> "Invalid Day";
        };

        System.out.println("Day: " + dayName);
    }
}