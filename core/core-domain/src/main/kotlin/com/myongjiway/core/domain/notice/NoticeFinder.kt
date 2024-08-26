package com.myongjiway.core.domain.notice

import com.myongjiway.core.domain.usernotice.UserNoticeRepository
import org.springframework.stereotype.Component

@Component
class NoticeFinder(
    private val noticeRepository: NoticeRepository,
    private val userNoticeRepository: UserNoticeRepository,
) {
    fun findNotice(noticeId: Long, userId: Long): NoticeView {
        val noticeView = noticeRepository.findById(noticeId)
        userNoticeRepository.findByUserIdAndNoticeId(userId, noticeId)
            ?: userNoticeRepository.save(noticeId, userId)
        noticeView.read = true
        return noticeView
    }

    fun findNotices(userId: Long): List<NoticeView> {
        val allNotices = noticeRepository.findAll()
        val readNotices = userNoticeRepository.findByUserId(userId).map { it.noticeId }.toSet()

        return allNotices.map { noticeView ->
            noticeView.read = readNotices.contains(noticeView.id)
            noticeView
        }
    }

    fun findNotices(): List<NoticeView> = noticeRepository.findAll()
}
