package com.myongjiway.notice

import java.time.LocalDateTime

class Notice(
    val id: Long?,
    var title: String,
    var content: String,
    var read: Boolean = false,
    val createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
) {
    companion object {
        fun fixture(
            id: Long? = null,
            title: String = "title",
            content: String = "content",
            read: Boolean = false,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
        ): Notice = Notice(
            id = id,
            title = title,
            content = content,
            read = read,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
