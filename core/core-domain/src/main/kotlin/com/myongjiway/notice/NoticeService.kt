package com.myongjiway.notice

import com.myongjiway.user.User

interface NoticeService {
    fun createNotice(notice: Notice, user: User): Unit
    fun updateNotice(noticeId: Long, notice: Notice, user: User): Unit
    fun deleteNotice(noticeId: Long, user: User): Unit
    fun getNotice(noticeId: Long, user: User): Notice
    fun getNotices(user: User): List<Notice>
}
