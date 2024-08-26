package com.myongjiway.core.domain.notice

import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeCreator: NoticeCreator,
    private val noticeUpdater: NoticeUpdater,
    private val noticeDeleter: NoticeDeleter,
    private val noticeFinder: NoticeFinder,
) {

    fun createNotice(noticeMetadata: NoticeMetadata) {
        noticeCreator.createNotice(noticeMetadata)
    }

    fun updateNotice(noticeMetadata: NoticeMetadata, noticeId: Long) {
        noticeUpdater.updateNotice(noticeMetadata, noticeId)
    }

    fun deleteNotice(noticeId: Long) {
        noticeDeleter.deleteNotice(noticeId)
    }

    fun getNotice(noticeId: Long, userId: Long): NoticeView = noticeFinder.findNotice(noticeId, userId)
    fun getNotices(userId: Long): List<NoticeView> = noticeFinder.findNotices(userId)
    fun getNotices(): List<NoticeView> = noticeFinder.findNotices()
}
