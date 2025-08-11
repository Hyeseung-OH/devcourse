package com.back;

import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input) {
        Scanner sc = TestUtil.genScanner(input + "\n종료");

        ByteArrayOutputStream outputStream = TestUtil.setOutToByteArray();
        new App(sc).run();

        return outputStream.toString();
    }
}
