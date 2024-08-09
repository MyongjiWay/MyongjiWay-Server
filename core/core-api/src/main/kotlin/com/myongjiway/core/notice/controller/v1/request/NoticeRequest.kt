package com.myongjiway.core.notice.controller.v1.request

import com.myongjiway.notice.Notice

data class NoticeRequest(
    val id: Long?,
    val title: String,
    val content: String,
) {
    fun toNotice(noticeId: Long?): Notice = Notice.fixture(
        id = noticeId,
        title = title,
        content = content,
    )
    init {
        NoticeValidator.validateTitle(title)
        NoticeValidator.validateContent(content)
    }
}
