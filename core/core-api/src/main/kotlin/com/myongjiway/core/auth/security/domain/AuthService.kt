package com.myongjiway.core.auth.security.domain

import com.myongjiway.token.RefreshData
import com.myongjiway.token.TokenAppender
import com.myongjiway.token.TokenGenerator
import com.myongjiway.token.TokenResult
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.UserAppender
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
