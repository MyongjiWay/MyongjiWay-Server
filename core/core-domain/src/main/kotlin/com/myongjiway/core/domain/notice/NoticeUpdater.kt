package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Component

@Component
class NoticeUpdater(
    private val noticeRepository: NoticeRepository,
) {
    fun updateNotice(notice: Notice, noticeId: Long) {
        noticeRepository.update(notice, noticeId)
    }
}
