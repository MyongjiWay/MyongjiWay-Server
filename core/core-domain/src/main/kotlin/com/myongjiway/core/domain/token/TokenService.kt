package com.myongjiway.core.domain.token

import com.myongjiway.core.domain.user.UserReader
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenGenerator: TokenGenerator,
    private val tokenReader: TokenReader,
    private val userReader: UserReader,
    private val tokenProcessor: TokenProcessor,
) {
    /**
     * RefreshToken을 이용하여 AccessToken을 재발급한다. RefreshToken 또한 만료되었다면 갱신한다.
     * @param refreshData AccessToken을 재발급하기 위한 RefreshToken
     * @return TokenResult 재발급된 AccessToken과 RefreshToken
     */
    fun refresh(refreshData: RefreshData): TokenResult {
        var refreshToken = tokenReader.find(refreshData.refreshToken)

        val user = userReader.find(refreshToken.userId.toLong())

        refreshToken = tokenGenerator.refresh(refreshToken)

        val newAccessToken = tokenGenerator.generateAccessTokenByUserId(user.id.toString())
        return TokenResult(newAccessToken.token, refreshToken.token)
    }

    fun delete(refreshToken: String) {
        val foundRefreshToken = tokenReader.find(refreshToken)
        tokenProcessor.deleteToken(foundRefreshToken.token)
    }
}
