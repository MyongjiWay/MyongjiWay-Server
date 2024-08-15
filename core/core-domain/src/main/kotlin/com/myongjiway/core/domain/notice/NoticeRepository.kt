package com.myongjiway.core.domain.notice

interface NoticeRepository {
    fun save(notice: Notice): Unit
    fun update(notice: Notice, noticeId: Long): Unit
    fun delete(noticeId: Long): Unit
    fun findById(noticeId: Long): Notice
    fun findAll(): List<Notice>
}
