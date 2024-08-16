package com.myongjiway.core.api.controller.v1.request

import com.myongjiway.core.domain.notice.NoticeMetadata

data class NoticeRequest(
    val title: String,
    val author: String,
    val content: String,
) {
    fun toMetaData(author: String): NoticeMetadata = NoticeMetadata(
        title = title,
        author = author,
        content = content,
    )
    init {
        NoticeValidator.validateTitle(title)
        NoticeValidator.validateContent(content)
    }
}
