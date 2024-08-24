package com.myongjiway.core.domain.token

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import org.springframework.stereotype.Component

@Component
class TokenReader(
    private val tokenRepository: TokenRepository,
) {
    fun find(token: String): Token = tokenRepository.find(token) ?: throw CoreException(CoreErrorType.TOKEN_NOT_FOUND)
}
