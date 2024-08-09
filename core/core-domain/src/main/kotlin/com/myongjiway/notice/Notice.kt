package com.myongjiway.notice

import java.time.LocalDateTime

class Notice(
    val id: Long?,
    var title: String,
    var author: String,
    var content: String,
    var read: Boolean = false,
    val createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
)
