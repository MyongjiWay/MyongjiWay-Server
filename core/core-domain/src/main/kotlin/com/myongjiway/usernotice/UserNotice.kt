package com.myongjiway.usernotice

import java.time.LocalDateTime

class UserNotice(
    val id: Long?,
    val noticeId: Long?,
    val createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
) {
    companion object {
        fun fixture(
            id: Long? = null,
            noticeId: Long? = null,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
        ): UserNotice = UserNotice(
            id = id,
            noticeId = noticeId,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
