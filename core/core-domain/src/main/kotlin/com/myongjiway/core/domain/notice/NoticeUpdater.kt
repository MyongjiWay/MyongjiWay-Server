package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Component

@Component
class NoticeUpdater(
    private val noticeRepository: NoticeRepository,
) {
    fun updateNotice(noticeMetadata: NoticeMetadata, noticeId: Long) {
        noticeRepository.update(noticeMetadata, noticeId)
    }
}
