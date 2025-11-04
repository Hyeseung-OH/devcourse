package com.back.domain.system.controller

import com.back.TestRunner
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class SystemControllerTest {

    @Test
    fun `종료`() {
        val out: String = TestRunner.run("")
        assertThat(out).contains("프로그램을 종료합니다.")
    }
}