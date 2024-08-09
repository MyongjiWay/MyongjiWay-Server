package com.myongjiway.core.notice.controller.v1.response

import com.myongjiway.notice.Notice
import java.time.LocalDateTime

data class NoticeResponse(
    val id: Long,
    val title: String,
    val author: String,
    val content: String,
    val read: Boolean = false,
    val createdAt: LocalDateTime?,
) {
    companion object {
        fun of(
            notice: Notice,
        ): NoticeResponse = NoticeResponse(
            id = notice.id!!,
            title = notice.title,
            author = notice.author,
            content = notice.content,
            read = notice.read,
            createdAt = notice.createdAt,
        )
    }
}
