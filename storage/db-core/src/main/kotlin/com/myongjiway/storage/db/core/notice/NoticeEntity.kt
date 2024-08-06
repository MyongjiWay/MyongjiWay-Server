package com.myongjiway.storage.db.core.notice

import com.myongjiway.notice.Notice
import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "notice")
class NoticeEntity(
    private var title: String,
    private var content: String,
) : BaseEntity() {
    fun toNotice() = Notice(
        id = id!!,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }
}
