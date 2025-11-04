package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying

interface WiseSayingRepository {

    fun save(wiseSaying: WiseSaying): WiseSaying
    fun findAll(): List<WiseSaying>
    fun findById(id: Int): WiseSaying?
    fun delete(wiseSaying: WiseSaying)
    fun clear()
    fun build()
    fun findByAuthorLike(keyword: String): List<WiseSaying>
    fun findByAuthorContent(keyword: String): List<WiseSaying>
}