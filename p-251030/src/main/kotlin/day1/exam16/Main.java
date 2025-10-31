package day1.exam16;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        try {
            copy("a.txt", "a_copy.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copy(String source, String target) throws IOException {
        Files.copy(new File(source).toPath(), new File(target).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}