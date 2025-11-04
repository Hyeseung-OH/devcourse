package com.back.domain.wiseSaying.service

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.domain.wiseSaying.repository.WiseSayingMemRepository
import com.back.domain.wiseSaying.repository.WiseSayingRepository
import com.back.global.bean.SingletonScope
import com.back.global.bean.SingletonScope.wiseSayingMemRepository

class WiseSayingService(
    val wiseSayingRepository: WiseSayingRepository = SingletonScope.wiseSayingFileRepository
) {

    fun write(content: String, author: String): WiseSaying =
        WiseSaying(content = content, author = author).also {
            return wiseSayingRepository.save(it)
        }

    fun findAll() = wiseSayingRepository.findAll()

    fun findById(id: Int): WiseSaying? =
        wiseSayingRepository.findById(id)

    fun delete(wiseSaying: WiseSaying) = wiseSayingRepository.delete(wiseSaying)

    fun modify(wiseSaying: WiseSaying, content: String, author: String) {
        wiseSaying.modify(content, author)
    }

    fun build() {
        wiseSayingRepository.build()
    }

    fun findByKeyword(keywordType: String, keyword: String): List<WiseSaying> {
        return when ( keywordType ) {
            "author" -> wiseSayingRepository.findByAuthorLike("%$keyword%")
            else -> wiseSayingRepository.findByAuthorContent("%$keyword%")
        }
    }
}