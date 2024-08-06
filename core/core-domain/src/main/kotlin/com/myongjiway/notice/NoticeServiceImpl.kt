package com.myongjiway.notice

import org.springframework.stereotype.Service

@Service
class NoticeServiceImpl(private val noticeRepository: NoticeRepository, private val userNoticeRepository: UserNoticeRepository) : NoticeService {
    override fun createNotice(notice: Notice) {
        noticeRepository.save(notice.title, notice.content)
    }

    override fun updateNotice(noticeId: Long, notice: Notice) {
        noticeRepository.update(noticeId, notice.title, notice.content)
    }

    override fun deleteNotice(noticeId: Long) {
        noticeRepository.delete(noticeId)
    }

    override fun getNotice(noticeId: Long): Notice {
        val userId = 1L
        val notice = noticeRepository.findById(noticeId)
        userNoticeRepository.findByUserIdAndNoticeId(userId, notice.id!!)
            ?: userNoticeRepository.save(noticeId, userId)
        notice.read = true
        return notice
    }

    override fun getNotices(): List<Notice> {
        val userId = 1L
        val allNotices = noticeRepository.findAll()
        val readNotices = userNoticeRepository.findByUserId(userId).map { it.noticeId }.toSet()

        return allNotices.map { notice ->
            notice.read = readNotices.contains(notice.id)
            notice
        }
    }
}
