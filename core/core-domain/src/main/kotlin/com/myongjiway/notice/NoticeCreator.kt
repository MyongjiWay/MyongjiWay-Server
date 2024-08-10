package com.myongjiway.notice

import org.springframework.stereotype.Component

@Component
class NoticeCreator(
    private val noticeRepository: NoticeRepository,
) {
    fun createNotice(notice: Notice) {
        noticeRepository.save(notice)
    }
}
