package com.myongjiway.notice

interface NoticeService {
    fun createNotice(notice: Notice): Unit
    fun updateNotice(noticeId: Long, notice: Notice): Unit
    fun deleteNotice(noticeId: Long): Unit
    fun getNotice(noticeId: Long): Notice
    fun getNotices(): List<Notice>
}
