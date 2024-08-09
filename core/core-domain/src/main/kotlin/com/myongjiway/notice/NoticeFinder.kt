package com.myongjiway.notice

import com.myongjiway.usernotice.UserNoticeRepository
import org.springframework.stereotype.Component

@Component
class NoticeFinder(
    private val noticeRepository: NoticeRepository,
    private val userNoticeRepository: UserNoticeRepository,
) {
    fun findNotice(noticeId: Long, userId: Long): Notice {
        val notice = noticeRepository.findById(noticeId)
        userNoticeRepository.findByUserIdAndNoticeId(userId, noticeId)
            ?: userNoticeRepository.save(noticeId, userId)
        notice.read = true
        return notice
    }

    fun findNotices(userId: Long): List<Notice> {
        val allNotices = noticeRepository.findAll()
        val readNotices = userNoticeRepository.findByUserId(userId).map { it.noticeId }.toSet()

        return allNotices.map { notice ->
            notice.read = readNotices.contains(notice.id)
            notice
        }
    }
}
