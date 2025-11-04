package com.back.domain.wiseSaying.service

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.domain.wiseSaying.repository.WiseSayingMemRepository
import com.back.global.bean.SingletonScope

class WiseSayingService(
    val wiseSayingMemRepository: WiseSayingMemRepository = SingletonScope.wiseSayingMemRepository
) {

    fun write(content: String, author: String): WiseSaying =
        WiseSaying(content = content, author = author).also {
            return wiseSayingMemRepository.save(it)
        }

    fun findAll() = wiseSayingMemRepository.findAll()

    fun findById(id: Int): WiseSaying? =
        wiseSayingMemRepository.findById(id)

    fun delete(wiseSaying: WiseSaying) = wiseSayingMemRepository.delete(wiseSaying)

    fun modify(wiseSaying: WiseSaying, content: String, author: String) {
        wiseSaying.modify(content, author)
    }
}