package com.myongjiway.core.domain.notice

import java.time.LocalDateTime

class NoticeView(
    var id: Long,
    var metadata: NoticeMetadata,
    var read: Boolean,
    var createdAt: LocalDateTime,
)
