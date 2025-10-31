package day1.exam19;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<String> greetings = names.stream()
                .map(name -> "Hello, " + name)
                .collect(Collectors.toList());

        greetings.forEach(System.out::println);
    }
}