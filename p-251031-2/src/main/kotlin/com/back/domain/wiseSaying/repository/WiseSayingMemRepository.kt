package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying

class WiseSayingMemRepository : WiseSayingRepository {

    var lastId = 0
    val wiseSayings = mutableListOf<WiseSaying>()

    override fun save(wiseSaying: WiseSaying): WiseSaying {

        return wiseSaying
            .takeIf { it.isNew() }
            .also {
                wiseSaying.id = ++lastId
                wiseSayings.add(wiseSaying)
            } ?: wiseSaying
    }

    override fun findAll() = wiseSayings.toList()

    override fun findById(id: Int): WiseSaying? = wiseSayings.firstOrNull { it.id == id }

    override fun delete(wiseSaying: WiseSaying) {
        wiseSayings.remove(wiseSaying)
    }

    override fun clear() {
        lastId = 0
        wiseSayings.clear()
    }

    override fun build() {
        println("WiseSayingMemRepository builded")
    }

    override fun findByAuthorLike(keyword: String): List<WiseSaying> {
        return listOf()
    }

    override fun findByAuthorContent(keyword: String): List<WiseSaying> {
        return listOf()
    }
}