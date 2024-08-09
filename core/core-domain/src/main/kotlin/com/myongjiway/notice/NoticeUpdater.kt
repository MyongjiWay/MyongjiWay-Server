package com.myongjiway.notice

import org.springframework.stereotype.Component

@Component
class NoticeUpdater(
    private val noticeRepository: NoticeRepository,
) {
    fun updateNotice(notice: Notice) {
        noticeRepository.update(notice)
    }
}
