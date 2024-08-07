package com.myongjiway.notice

interface NoticeRepository {
    fun save(title: String, content: String): Unit
    fun update(noticeId: Long, title: String, content: String): Unit
    fun delete(noticeId: Long): Unit
    fun findById(noticeId: Long): Notice
    fun findAll(): List<Notice>
}
