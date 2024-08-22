package com.myongjiway.core.domain.token

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
        var refreshToken = tokenReader.find(refreshData.refreshToken)

        val user = userReader.find(refreshToken.userId.toLong())

        if (isExpired(refreshToken)) {
            refreshToken = tokenGenerator.generateRefreshTokenByUserId(user.id.toString())
            tokenAppender.upsert(user.id, refreshToken.token, refreshToken.expiration)
        }

        val newAccessToken = tokenGenerator.generateAccessTokenByUserId(user.id.toString())
        return TokenResult(newAccessToken.token, refreshToken.token)
    }

    fun delete(refreshToken: String) {
        val foundRefreshToken = tokenReader.find(refreshToken)
        tokenProcessor.deleteToken(foundRefreshToken.token)
    }

    private fun isExpired(refreshToken: Token?): Boolean = refreshToken?.expiration!! <= System.currentTimeMillis()
}
