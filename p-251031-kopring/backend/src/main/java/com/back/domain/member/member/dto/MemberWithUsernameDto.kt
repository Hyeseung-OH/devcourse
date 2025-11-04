package com.back.domain.member.member.dto

import com.back.domain.member.member.entity.Member
import java.time.LocalDateTime

@JvmRecord
data class MemberWithUsernameDto private constructor(
    val id: Long?,
    val createDate: LocalDateTime?,
    val modifyDate: LocalDateTime?,
    val username: String?,
    val nickname: String?,
    val isAdmin: Boolean

) {
    constructor(member: Member) : this(
        member.getId(),
        member.getCreateDate(),
        member.getModifyDate(),
        member.getUsername(),
        member.getNickname(),
        member.isAdmin()
    )
}