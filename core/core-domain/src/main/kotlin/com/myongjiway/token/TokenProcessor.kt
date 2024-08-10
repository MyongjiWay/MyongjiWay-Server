package com.myongjiway.token

import org.springframework.stereotype.Component

@Component
class TokenProcessor(
    private val tokenRepository: TokenRepository,
) {
    fun deleteToken(refreshToken: String) {
        tokenRepository.delete(refreshToken)
    }
}
