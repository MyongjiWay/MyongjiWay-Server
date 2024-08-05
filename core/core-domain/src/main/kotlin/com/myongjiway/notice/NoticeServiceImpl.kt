package com.myongjiway.notice

import com.myongjiway.error.CoreErrorType
import com.myongjiway.error.CoreException
import com.myongjiway.user.Role
import com.myongjiway.user.User
import org.springframework.stereotype.Service

@Service
class NoticeServiceImpl(private val noticeRepository: NoticeRepository, private val userNoticeRepository: UserNoticeRepository) : NoticeService {
    override fun createNotice(notice: Notice, user: User) {
        if (user.role != Role.ADMIN) {
            throw CoreException(CoreErrorType.UNAUTHORIZED)
        }

        noticeRepository.save(notice.title, notice.content)
    }

    override fun updateNotice(noticeId: Long, notice: Notice, user: User) {
        if (user.role != Role.ADMIN) {
            throw CoreException(CoreErrorType.UNAUTHORIZED)
        }
        noticeRepository.update(noticeId, notice.title, notice.content)
    }

    override fun deleteNotice(noticeId: Long, user: User) {
        if (user.role != Role.ADMIN) {
            throw CoreException(CoreErrorType.UNAUTHORIZED)
        }
        noticeRepository.delete(noticeId)
    }

    override fun getNotice(noticeId: Long, user: User): Notice {
        val notice = noticeRepository.findById(noticeId)
        userNoticeRepository.findByUserIdAndNoticeId(user.id!!, notice.id!!)
            ?: userNoticeRepository.save(noticeId, user.id!!)
        notice.read = true
        return notice
    }

    override fun getNotices(user: User): List<Notice> {
        val allNotices = noticeRepository.findAll()
        val readNotices = userNoticeRepository.findByUserId(user.id!!).map { it.noticeId }.toSet()

        return allNotices.map { notice ->
            notice.read = readNotices.contains(notice.id)
            notice
        }
    }
}
