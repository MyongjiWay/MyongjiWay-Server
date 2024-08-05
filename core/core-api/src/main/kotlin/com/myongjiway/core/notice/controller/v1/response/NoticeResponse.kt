package com.myongjiway.core.notice.controller.v1.response

import com.myongjiway.notice.Notice

data class NoticeResponse(
    val id: Long,
    val title: String,
    val content: String,
    val read: Boolean = false,
) {
    companion object {
        fun of(
            notice: Notice,
        ): NoticeResponse = NoticeResponse(
            id = notice.id!!,
            title = notice.title,
            content = notice.content,
            read = notice.read,
        )
    }
}
