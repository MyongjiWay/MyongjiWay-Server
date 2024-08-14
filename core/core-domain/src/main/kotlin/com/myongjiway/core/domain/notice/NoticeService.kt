package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeCreator: com.myongjiway.core.domain.notice.NoticeCreator,
    private val noticeUpdater: com.myongjiway.core.domain.notice.NoticeUpdater,
    private val noticeDeleter: com.myongjiway.core.domain.notice.NoticeDeleter,
    private val noticeFinder: com.myongjiway.core.domain.notice.NoticeFinder,
) {

    fun createNotice(notice: com.myongjiway.core.domain.notice.Notice) {
        noticeCreator.createNotice(notice)
    }
    fun updateNotice(notice: com.myongjiway.core.domain.notice.Notice, noticeId: Long) {
        noticeUpdater.updateNotice(notice, noticeId)
    }
    fun deleteNotice(noticeId: Long) {
        noticeDeleter.deleteNotice(noticeId)
    }
    fun getNotice(noticeId: Long, userId: Long): com.myongjiway.core.domain.notice.Notice = noticeFinder.findNotice(noticeId, userId)
    fun getNotices(userId: Long): List<com.myongjiway.core.domain.notice.Notice> = noticeFinder.findNotices(userId)
}
