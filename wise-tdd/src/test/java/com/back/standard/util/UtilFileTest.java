package com.back.standard.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilFileTest {
    @Test
    @DisplayName("파일 생성")
    void t1() {
        // 무언가를 세팅하고
        String filePath = "test.txt";

        // 수행하면
        Util.file.touch(filePath);

        // 결과가 나온다 => 실제 파일이 존재하는가?
        boolean rst = Util.file.exists(filePath);

        assertThat(rst).isTrue();
    }
}
