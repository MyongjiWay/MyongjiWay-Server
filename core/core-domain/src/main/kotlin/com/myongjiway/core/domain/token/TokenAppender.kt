package com.myongjiway.core.domain.token

import org.springframework.stereotype.Component

@Component
class TokenAppender(
    private val tokenRepository: TokenRepository,
) {
    fun upsert(userId: Long, token: String, expiration: Long): Long = tokenRepository.upsert(userId, token, expiration)
}
