package com.myongjiway.core.notice.controller.v1.response

import com.myongjiway.notice.Notice
import java.time.LocalDate

data class NoticeResponse(
    val id: Long,
    val author: String,
    val title: String,
    val content: String,
    val read: Boolean = false,
    val createdAt: LocalDate?,
) {
    companion object {
        fun of(
            notice: Notice,
        ): NoticeResponse = NoticeResponse(
            id = notice.id!!,
            author = notice.author,
            title = notice.title,
            content = notice.content,
            read = notice.read,
            createdAt = notice.createdAt?.toLocalDate(),
        )
    }
}
