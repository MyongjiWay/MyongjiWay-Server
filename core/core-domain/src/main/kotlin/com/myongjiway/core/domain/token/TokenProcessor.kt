package com.myongjiway.core.domain.token

import org.springframework.stereotype.Component

@Component
class TokenProcessor(
    private val tokenRepository: TokenRepository,
) {
    fun deleteToken(refreshToken: String) {
        tokenRepository.delete(refreshToken)
    }
}
