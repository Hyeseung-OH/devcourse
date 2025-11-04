package com.back.domain.post.post.dto

import com.back.domain.post.post.entity.Post
import java.time.LocalDateTime

@JvmRecord
data class PostDto private constructor(
    val id: Long?,
    val createDate: LocalDateTime?,
    val modifyDate: LocalDateTime?,
    val title: String?,
    val content: String?,
    val authorId: Long?,
    val authorName: String?
) {
    constructor(post: Post) : this(
        post.getId(),
        post.getCreateDate(),
        post.getModifyDate(),
        post.getTitle(),
        post.getContent(),
        post.getAuthor().getId(),
        post.getAuthor().getName()
    )
}
