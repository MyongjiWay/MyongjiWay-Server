package com.myongjiway.core.notice.controller.v1.request

import com.myongjiway.notice.Notice

data class NoticeRequest(
    val title: String,
    val content: String,
) {
    fun toNotice(): Notice = Notice(
        id = null,
        title = title,
        author = "admin",
        content = content,
        read = false,
        createdAt = null,
        updatedAt = null,
    )
    init {
        NoticeValidator.validateTitle(title)
        NoticeValidator.validateContent(content)
    }
}
