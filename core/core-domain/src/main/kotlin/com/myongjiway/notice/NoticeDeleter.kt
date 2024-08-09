package com.myongjiway.notice

import org.springframework.stereotype.Component

@Component
class NoticeDeleter(
    private val noticeRepository: NoticeRepository,
) {
    fun deleteNotice(noticeId: Long) {
        noticeRepository.delete(noticeId)
    }
}
