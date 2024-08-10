package com.myongjiway.token

import com.myongjiway.error.CoreErrorType
import com.myongjiway.error.CoreException
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenAppender: TokenAppender,
    private val tokenGenerator: TokenGenerator,
    private val tokenReader: TokenReader,
) {
    fun refresh(refreshData: RefreshData): TokenResult {
        var refreshToken = tokenReader.findByTokenAndUserId(refreshData.userId, refreshData.refreshToken)
            ?: throw CoreException(CoreErrorType.UNAUTHORIZED_TOKEN)

        val isExpired = validateRefreshToken(refreshToken)

        if (isExpired) {
            refreshToken = tokenGenerator.generateRefreshTokenByUserId(refreshData.userId.toString())
            tokenAppender.upsert(refreshData.userId, refreshToken.token, refreshToken.expiration)
        }

        val newAccessToken = tokenGenerator.generateAccessTokenByUserId(refreshData.userId.toString())
        return TokenResult(newAccessToken.token, refreshToken.token)
    }

    private fun validateRefreshToken(refreshToken: Token?): Boolean = refreshToken?.expiration!! <= System.currentTimeMillis()
}
