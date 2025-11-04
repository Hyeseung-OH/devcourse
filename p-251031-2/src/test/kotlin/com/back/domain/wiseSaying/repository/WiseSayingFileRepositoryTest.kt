package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.global.appConfig.AppConfig
import com.back.global.bean.SingletonScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class WiseSayingFileRepositoryTest {
    private val wiseSayingRepository = SingletonScope.wiseSayingFileRepository

    companion object {

        @JvmStatic
        @BeforeAll
        fun setMode() {
            AppConfig.setModeToTest()
        }
    }

    @BeforeEach
    fun setUp() {
        wiseSayingRepository.clear()
    }

    @Test
    fun `save`() {
        val wiseSaying = wiseSayingRepository
            .save(WiseSaying(content = "나의 죽음을 적들에게 알리지 말라.", author = "충무공 이순신"))

        val filePath = wiseSayingRepository
            .tableDirPath
            .toFile()
            .listFiles()
            ?.find { it.name == "${wiseSaying.id}.json" }

        assertThat(filePath).isNotNull
    }

    @Test
    fun `findAll`() {
        val wiseSaying1 = wiseSayingRepository
            .save(WiseSaying("나의 죽음을 적들에게 알리지 말라.", "충무공 이순신"))

        val wiseSaying2 = wiseSayingRepository
            .save(WiseSaying("나를 파괴할 수 있는 사람이 없다.", "바토르"))

        val foundWiseSayings = wiseSayingRepository.findAll()

        assertThat(foundWiseSayings).containsExactly(wiseSaying1, wiseSaying2)
    }

    @Test
    fun `findById`() {
        val wiseSaying = wiseSayingRepository
            .save(WiseSaying("나의 죽음을 적들에게 알리지 말라.", "충무공 이순신"))

        val foundWiseSaying = wiseSayingRepository.findById(wiseSaying.id)

        assertThat(foundWiseSaying).isEqualTo(wiseSaying)
    }

    @Test
    fun `delete`() {
        val wiseSaying = wiseSayingRepository
            .save(WiseSaying("나의 죽음을 적들에게 알리지 말라.", "충무공 이순신"))

        wiseSayingRepository.delete(wiseSaying)

        assertThat(wiseSayingRepository.findById(wiseSaying.id)).isNull()
    }

    @Test
    fun `saveLastId, loadLastId`() {
        wiseSayingRepository.saveLastId(10)
        assertThat(wiseSayingRepository.loadLastId()).isEqualTo(10)
    }
}