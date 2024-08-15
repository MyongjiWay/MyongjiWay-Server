package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Component

@Component
class NoticeCreator(
    private val noticeRepository: com.myongjiway.core.domain.notice.NoticeRepository,
) {
    fun createNotice(notice: com.myongjiway.core.domain.notice.Notice) {
        noticeRepository.save(notice)
    }
}
