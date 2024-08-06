package com.myongjiway.storage.db.core.token

import java.time.LocalDateTime

class TokenEntityProxy(
    override val id: Long,
    override val createdAt: LocalDateTime,
    override var updatedAt: LocalDateTime?,
    userId: Long,
    token: String,
    expiration: Long,
) : TokenEntity(
    userId = userId,
    token = token,
    expiration = expiration,
)
