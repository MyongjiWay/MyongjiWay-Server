package com.myongjiway.storage.db.core.notice

import com.myongjiway.domain.notice.Notice
import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "notice")
class NoticeEntity(
    private var title: String,
    private var content: String,
    private var author: String,
) : BaseEntity() {
    fun toNotice() = Notice(
        id = id!!,
        title = title,
        content = content,
        author = author,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun update(title: String, content: String, author: String) {
        this.title = title
        this.content = content
        this.author = author
    }
}
