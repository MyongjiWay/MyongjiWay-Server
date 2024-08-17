package com.myongjiway.core.api.controller.v1.response

import com.myongjiway.core.domain.notice.NoticeView
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
            noticeView: NoticeView,
        ): NoticeResponse = NoticeResponse(
            id = noticeView.id,
            author = noticeView.metadata.author,
            title = noticeView.metadata.title,
            content = noticeView.metadata.content,
            read = noticeView.read,
            createdAt = noticeView.createdAt.toLocalDate(),
        )
    }
}
