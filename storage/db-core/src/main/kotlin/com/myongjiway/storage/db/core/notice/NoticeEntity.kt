package com.myongjiway.storage.db.core.notice

import com.myongjiway.notice.Notice
import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "notice")
class NoticeEntity(
    private var title: String,
    private var author: String,
    private var content: String,
) : BaseEntity() {
    fun toNotice() = Notice(
        id = id!!,
        title = title,
        author = author,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun update(title: String, author: String, content: String) {
        this.title = title
        this.author = author
        this.content = content
    }
}
