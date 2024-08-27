package com.myongjiway.storage.db.core.notice

import org.springframework.data.jpa.repository.JpaRepository

interface NoticeJpaRepository : JpaRepository<NoticeEntity, Long> {
    fun findByIdAndIsDeleted(id: Long, deleted: Boolean): NoticeEntity?
    fun findAllByIsDeleted(deleted: Boolean): List<NoticeEntity>
}
