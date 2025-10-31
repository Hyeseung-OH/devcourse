package com.back.domain.wiseSaying.entity

data class WiseSaying(
    val id: Int,
    var content: String,
    var author: String
) {
    fun modify(content: String, author: String) {
        this.content = content
        this.author = author
    }
}