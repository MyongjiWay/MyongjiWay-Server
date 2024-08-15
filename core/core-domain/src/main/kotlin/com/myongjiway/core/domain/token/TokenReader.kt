package com.myongjiway.core.domain.token

import org.springframework.stereotype.Component

@Component
class TokenReader(
    private val tokenRepository: TokenRepository,
) {
    fun findByToken(token: String): Token? = tokenRepository.find(token)
}
