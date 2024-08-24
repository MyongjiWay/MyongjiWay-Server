package com.myongjiway.core.domain.auth

import com.myongjiway.core.domain.token.RefreshData
import com.myongjiway.core.domain.token.TokenAppender
import com.myongjiway.core.domain.token.TokenGenerator
import com.myongjiway.core.domain.token.TokenResult
import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.UserAppender
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userAppender: UserAppender,
    private val tokenAppender: TokenAppender,
    private val tokenGenerator: TokenGenerator,
) {
    fun kakaoLogin(toKakaoLoginData: KakaoLoginData): TokenResult {
        val userId = userAppender.upsert(
            toKakaoLoginData.providerId,
            toKakaoLoginData.profileImg,
            toKakaoLoginData.username,
            ProviderType.KAKAO,
            Role.USER,
        )

        val accessToken = tokenGenerator.generateAccessTokenByUserId(userId.toString())
        val refreshToken = tokenGenerator.generateRefreshTokenByUserId(userId.toString())

        tokenAppender.upsert(userId, refreshToken.token, refreshToken.expiration)
        return TokenResult(accessToken.token, refreshToken.token)
    }

    fun refresh(refreshData: RefreshData): TokenResult {
        TODO("Not yet implemented")
    }
}
