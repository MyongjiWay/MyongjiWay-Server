package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Component

@Component
class NoticeCreator(
    private val noticeRepository: NoticeRepository,
) {
    fun createNotice(noticeMetadata: NoticeMetadata) {
        noticeRepository.save(noticeMetadata)
    }
}
