package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    @Test
    @DisplayName("=== 명언 앱 === 출력")
    void t1() {
        String out = AppTestRunner.run("종료");
        assertThat(out).contains("== 명언 앱 ==");
    }

    @Test
    @DisplayName("등록")
    void t2() {
        String out = AppTestRunner.run(
                """
                        등록
                        현재를 사랑하라.
                        작자미상
                        """
        );
        assertThat(out).contains("명령) ");
        assertThat(out).contains("명언 : ");
        assertThat(out).contains("작가 : ");
    }
}
