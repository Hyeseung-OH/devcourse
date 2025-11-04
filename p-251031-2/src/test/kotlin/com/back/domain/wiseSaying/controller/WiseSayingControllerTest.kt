package com.back.domain.wiseSaying.controller

import com.back.TestRunner
import com.back.global.bean.SingletonScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertContains

class WiseSayingControllerTest {

    @BeforeEach
    fun clear() {
        SingletonScope.wiseSayingMemRepository.clear()
    }

    @Test
    fun `명언 작성`() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
        """
        )
        println("result: $result")
        assertContains(result, "명언: ")
        assertContains(result, "작가: ")
        assertContains(result, "1번 명언이 등록되었습니다.")
    }

    @Test
    fun `명언 목록`() {
        val result = TestRunner.run(
            """
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                """
        )
        println("result: $result")

        assertThat(result)
            .contains("번호 / 작가 / 명언")
            .contains("----------------------")
            .contains("2 / 작자미상 / 과거에 집착하지 마라.")
            .contains("1 / 작자미상 / 현재를 사랑하라.")
    }

    @Test
    fun `삭제?id=1`() {
        val out = TestRunner.run(
            """
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                
                """.trimIndent()
        )

        assertThat(out)
            .contains("1번 명언이 삭제되었습니다.")
            .contains("2 / 작자미상 / 과거에 집착하지 마라.")
            .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
    }

    @Test
    fun `수정id=1`() {
        val out = TestRunner.run(
            """
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                너 자신을 알라
                소크라테스
                목록 
                """.trimIndent()
        )

        assertThat(out)
            .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
            .contains("1 / 소크라테스 / 너 자신을 알라")
    }
}