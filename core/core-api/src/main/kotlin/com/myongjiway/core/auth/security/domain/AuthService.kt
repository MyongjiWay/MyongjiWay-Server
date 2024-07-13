package com.myongjiway.core.auth.security.domain

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.UserAppender
import com.myongjiway.user.UserFinder
import com.myongjiway.user.UserUpdater
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val userFinder: UserFinder,
    private val userUpdater: UserUpdater,
    private val userAppender: UserAppender,
) {
    fun kakaoLogin(toKakaoLoginData: KakaoLoginData): TokenResult {
        val user = userFinder.find(toKakaoLoginData.providerId)

        val userId: Long = if (user == null) {
            userAppender.append(
                toKakaoLoginData.providerId,
                toKakaoLoginData.profileImg,
                toKakaoLoginData.username,
                ProviderType.KAKAO,
                Role.USER,
            )
        } else {
            if (user.profileImg != toKakaoLoginData.profileImg || user.name != toKakaoLoginData.username) {
                userUpdater.modify(
                    providerId = toKakaoLoginData.providerId,
                    profileImg = toKakaoLoginData.profileImg,
                    name = toKakaoLoginData.username,
                    role = Role.USER,
                )
            }
            user.id!!
        }

        val accessToken = jwtProvider.generateAccessTokenByUserId(userId.toString())
        val refreshToken = jwtProvider.generateRefreshTokenByUserId(userId.toString())

        return TokenResult(accessToken.token, refreshToken.token)
    }
}
