package com.myongjiway.storage.db.core.notice

import org.springframework.data.jpa.repository.JpaRepository

interface UserNoticeJpaRepository : JpaRepository<UserNoticeEntity, Long> {
    fun findByUserIdAndNoticeId(userId: Long, noticeId: Long): UserNoticeEntity?
    fun findByUserId(userId: Long): List<UserNoticeEntity>
    fun deleteAllByNoticeId(noticeId: Long)
}
