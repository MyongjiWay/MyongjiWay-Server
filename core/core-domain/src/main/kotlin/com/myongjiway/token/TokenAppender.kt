package com.myongjiway.token

import org.springframework.stereotype.Component

@Component
class TokenAppender(
    private val tokenRepository: TokenRepository,
) {
}
