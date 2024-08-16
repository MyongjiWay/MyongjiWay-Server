package com.myongjiway.storage.db.core.notice

import com.myongjiway.core.domain.notice.NoticeMetadata
import com.myongjiway.core.domain.notice.NoticeView
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
    fun toNoticeView() = NoticeView(
        id = id!!,
        metadata = NoticeMetadata(title, author, content),
        read = false,
        createdAt = createdAt!!,
    )

    fun update(title: String, author: String, content: String) {
        this.title = title
        this.author = author
        this.content = content
    }

    fun inactive() {
        this.isDeleted = true
    }
}
