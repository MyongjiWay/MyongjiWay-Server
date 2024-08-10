package com.myongjiway.token

import com.myongjiway.error.CoreErrorType
import com.myongjiway.error.CoreException
import com.myongjiway.user.UserReader
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenAppender: TokenAppender,
    private val tokenGenerator: TokenGenerator,
    private val tokenReader: TokenReader,
    private val userReader: UserReader,
) {
    fun refresh(refreshData: RefreshData): TokenResult {
        var refreshToken = tokenReader.findByToken(refreshData.refreshToken)
            ?: throw CoreException(CoreErrorType.UNAUTHORIZED_TOKEN)

        val user = userReader.find(refreshToken.userId.toLong())
            ?: throw CoreException(CoreErrorType.USER_NOT_FOUND)

        if (isExpired(refreshToken)) {
            refreshToken = tokenGenerator.generateRefreshTokenByUserId(user.id.toString())
            tokenAppender.upsert(user.id, refreshToken.token, refreshToken.expiration)
        }

        val newAccessToken = tokenGenerator.generateAccessTokenByUserId(user.id.toString())
        return TokenResult(newAccessToken.token, refreshToken.token)
    }

    private fun isExpired(refreshToken: Token?): Boolean = refreshToken?.expiration!! <= System.currentTimeMillis()
}
