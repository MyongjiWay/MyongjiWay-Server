package com.myongjiway.domain.notice

import java.time.LocalDateTime

class Notice(
    private val id: Long?,
    private var title: String,
    private var content: String,
    private var author: String,
    private val createdAt: LocalDateTime?,
    private val updatedAt: LocalDateTime?,
)
