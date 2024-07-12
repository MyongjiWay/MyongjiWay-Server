package com.myongjiway.core.auth.security.domain

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
) {
    fun kakaoLogin(toKakaoLoginData: KakaoLoginData): TokenResult {
        val user = userRepository.findUserByProviderId(toKakaoLoginData.providerId)

        val userId: Long = if (user == null) {
            userRepository.append(
                providerId = toKakaoLoginData.providerId,
                profileImg = toKakaoLoginData.profileImg,
                name = toKakaoLoginData.username,
                providerType = ProviderType.KAKAO,
                role = Role.USER,
            )
        } else {
            user.id!!
        }

        val accessToken = jwtProvider.generateAccessTokenByUserId(userId.toString())
        val refreshToken = jwtProvider.generateRefreshTokenByUserId(userId.toString())

        return TokenResult(accessToken.token, refreshToken.token)
    }
}
