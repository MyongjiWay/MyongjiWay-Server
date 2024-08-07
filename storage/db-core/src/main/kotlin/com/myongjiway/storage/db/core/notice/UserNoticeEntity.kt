package com.myongjiway.storage.db.core.notice

import com.myongjiway.notice.UserNotice
import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user_notice")
class UserNoticeEntity(
    private var userId: Long?,
    private var noticeId: Long?,
) : BaseEntity() {
    fun toUserNotice() = UserNotice(
        id = id!!,
        noticeId = noticeId!!,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
