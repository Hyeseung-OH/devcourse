package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {
    @Test
    @DisplayName("등록")
    void t1() {
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

    @Test
    @DisplayName("등록 시 번호 노출")
    void t2() {
        String out = AppTestRunner.run(
                """
                        등록
                        현재를 사랑하라.
                        작자미상
                        """
        );
        assertThat(out).contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 시 번호 노출")
    void t3() {
        String out = AppTestRunner.run(
                """
                        등록
                        현재를 사랑하라.
                        작자미상
                        등록
                        현재를 사랑하라.
                        작자미상
                        """
        );
        assertThat(out).contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 시 번호 노출")
    void t4() {
        String out = AppTestRunner.run(
                """
                        등록
                        현재를 사랑하라.
                        작자미상
                        등록
                        과거에 집착하지 마라.
                        작자미상
                        목록
                        """
        ); // chaining 가능
        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제?id=1")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                """);

        System.out.println(out);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");

    }

    @Test
    @DisplayName("삭제?id=1")
    void t6() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                삭제?id=1
                목록
                """);

        System.out.println(out);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정?id=3, 없는 명언에 대한 수정 요청")
    void t7() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=3
                """);

        System.out.println(out);

        assertThat(out)
                .contains("3번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정?id=8, 없는 명언에 대한 수정 요청")
    void t8() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                너 자신을 알라.
                소크라테스
                목록
                """);

        System.out.println(out);

        assertThat(out)
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
                .contains("1 / 소크라테스 / 너 자신을 알라.");
    }

    @Test
    @DisplayName("목록?keyword=과거")
    void t9() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록?keyword=과거
                """);

        System.out.println(out);

        assertThat(out)
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.");
    }
}
