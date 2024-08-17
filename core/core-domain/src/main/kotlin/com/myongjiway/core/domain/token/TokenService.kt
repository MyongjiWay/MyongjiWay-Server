package com.myongjiway.core.domain.token

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.user.UserReader
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenAppender: TokenAppender,
    private val tokenGenerator: TokenGenerator,
    private val tokenReader: TokenReader,
    private val userReader: UserReader,
    private val tokenProcessor: TokenProcessor,
) {
    fun refresh(refreshData: RefreshData): TokenResult {
        var refreshToken = tokenReader.findByToken(refreshData.refreshToken)
            ?: throw com.myongjiway.core.domain.error.CoreException(CoreErrorType.UNAUTHORIZED_TOKEN)

        val user = userReader.find(refreshToken.userId.toLong())
            ?: throw com.myongjiway.core.domain.error.CoreException(CoreErrorType.USER_NOT_FOUND)

        if (isExpired(refreshToken)) {
            refreshToken = tokenGenerator.generateRefreshTokenByUserId(user.id.toString())
            tokenAppender.upsert(user.id, refreshToken.token, refreshToken.expiration)
        }

        val newAccessToken = tokenGenerator.generateAccessTokenByUserId(user.id.toString())
        return TokenResult(newAccessToken.token, refreshToken.token)
    }

    fun delete(refreshToken: String) {
        val findRefreshToken = (
            tokenReader.findByToken(refreshToken)
                ?: throw com.myongjiway.core.domain.error.CoreException(CoreErrorType.NOT_FOUND_TOKEN)
            )

        tokenProcessor.deleteToken(findRefreshToken.token)
    }

    private fun isExpired(refreshToken: Token?): Boolean = refreshToken?.expiration!! <= System.currentTimeMillis()
}
