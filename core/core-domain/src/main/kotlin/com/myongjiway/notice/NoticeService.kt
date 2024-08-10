package com.myongjiway.notice

import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeCreator: NoticeCreator,
    private val noticeUpdater: NoticeUpdater,
    private val noticeDeleter: NoticeDeleter,
    private val noticeFinder: NoticeFinder,
) {

    fun createNotice(notice: Notice) {
        noticeCreator.createNotice(notice)
    }
    fun updateNotice(notice: Notice, noticeId: Long) {
        noticeUpdater.updateNotice(notice, noticeId)
    }
    fun deleteNotice(noticeId: Long) {
        noticeDeleter.deleteNotice(noticeId)
    }
    fun getNotice(noticeId: Long, userId: Long): Notice = noticeFinder.findNotice(noticeId, userId)
    fun getNotices(userId: Long): List<Notice> = noticeFinder.findNotices(userId)
}
