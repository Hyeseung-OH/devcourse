package com.back.domain.post.comment.dto

import com.back.domain.post.comment.entity.Comment
import java.time.LocalDateTime

// Record로 작성한 자프링은 변환 그대로 사용하면 되고
//@JvmRecord
//data class CommentDto(
//    val id: Long?,
//    val createDate: LocalDateTime?,
//    val modifyDate: LocalDateTime?,
//    val content: String?,
//    val authorId: Long?,
//    val authorName: String?,
//    val postId: Long?
//) {
//    constructor(comment: Comment) : this(
//        comment.getId(),
//        comment.getCreateDate(),
//        comment.getModifyDate(),
//        comment.getContent(),
//        comment.getAuthor().getId(),
//        comment.getAuthor().getName(),
//        comment.getPost().getId()
//    )
//}


// Class는 아래처럼 변환해야 함
class CommentDto private constructor(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val postId: Long
) {
    constructor(comment: Comment) : this(
        comment.id,
        comment.createDate,
        comment.modifyDate,
        comment.content,
        comment.author.id,
        comment.author.name,
        comment.post.id
    )
}