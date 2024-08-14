package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Component

@Component
class NoticeDeleter(
    private val noticeRepository: com.myongjiway.core.domain.notice.NoticeRepository,
) {
    fun deleteNotice(noticeId: Long) {
        noticeRepository.delete(noticeId)
    }
}
