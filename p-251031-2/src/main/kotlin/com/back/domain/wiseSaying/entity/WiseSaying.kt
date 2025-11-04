package com.back.domain.wiseSaying.entity

import com.back.standard.ut.JsonUtil

data class WiseSaying(
    var id: Int = 0,
    var content: String,
    var author: String
) {
    constructor(content: String, author: String) : this(0, content, author)

    fun modify(content: String, author: String) {
        this.content = content
        this.author = author
    }

    fun isNew(): Boolean {
        return id == 0
    }

    val jsonStr: String
        get() {
            return """
                {
                    "id": $id,
                    "content": "$content",
                    "author": "$author"
                }
            """.trimIndent()
        }

    val map: Map<String, Any>
        get() {
            return mapOf(
                "id" to id,
                "content" to content,
                "author" to author,
            )
        }

    companion object {
        fun fromJsonStr(jsonStr: String): WiseSaying {
            val map = JsonUtil.jsonStrToMap(jsonStr)

            return WiseSaying(
                id = map["id"] as Int,
                content = map["content"] as String,
                author = map["author"] as String,
            )
        }
    }
}