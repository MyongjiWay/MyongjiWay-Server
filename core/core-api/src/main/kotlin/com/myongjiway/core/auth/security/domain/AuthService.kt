package com.myongjiway.core.auth.security.domain

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.UserAppender
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val userAppender: UserAppender,
) {
    fun kakaoLogin(toKakaoLoginData: KakaoLoginData): TokenResult {
        val userId = userAppender.upsert(
            toKakaoLoginData.providerId,
            toKakaoLoginData.profileImg,
            toKakaoLoginData.username,
            ProviderType.KAKAO,
            Role.USER,
        )

        val accessToken = jwtProvider.generateAccessTokenByUserId(userId.toString())
        val refreshToken = jwtProvider.generateRefreshTokenByUserId(userId.toString())

        return TokenResult(accessToken.token, refreshToken.token)
    }
}
