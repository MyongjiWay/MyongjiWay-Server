package com.myongjiway.core.notice.controller.v1.request

import com.myongjiway.notice.Notice

data class NoticeRequest(
    val title: String,
    val content: String,
) {
    fun toNotice(): Notice = Notice.fixture(
        title = title,
        content = content,
    )
    init {
        NoticeValidator.validateTitle(title)
        NoticeValidator.validateContent(content)
    }
}
