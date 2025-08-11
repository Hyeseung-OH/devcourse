package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    @Test
    @DisplayName("=== 명언 앱 === 출력")
    void t1() {
        Scanner sc = TestUtil.genScanner("종료");

        ByteArrayOutputStream outputStream = TestUtil.setOutToByteArray();
        new App(sc).run();
        String out = outputStream.toString();

        assertThat(out).contains("== 명언 앱 ==");
    }
}
