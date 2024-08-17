package com.myongjiway.core.domain.notice

interface NoticeRepository {
    fun save(noticeMetadata: NoticeMetadata): Unit
    fun update(noticeMetadata: NoticeMetadata, noticeId: Long): Unit
    fun delete(noticeId: Long): Unit
    fun findById(noticeId: Long): NoticeView
    fun findAll(): List<NoticeView>
}
