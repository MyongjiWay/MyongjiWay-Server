package com.myongjiway.token

import org.springframework.stereotype.Component

@Component
class TokenReader(
    private val tokenRepository: TokenRepository,
) {
    fun findByTokenAndUserId(userId: Long, token: String): Token? = tokenRepository.find(userId, token)
}
