package com.myongjiway.notice

interface UserNoticeRepository {
    fun save(noticeId: Long, userId: Long): UserNotice
    fun findByUserIdAndNoticeId(userId: Long, noticeId: Long): UserNotice?
    fun findByUserId(userId: Long): List<UserNotice>
    fun deleteByNoticeId(noticeId: Long)
}
