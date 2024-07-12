package com.myongjiway.core.auth.security.jwt

import com.myongjiway.user.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
) {
    fun kakaoLogin(toKakaoLoginData: KakaoLoginData): TokenResult {
        val user = userRepository.findUserByProviderId(toKakaoLoginData.providerId)
        TODO("Not yet implemented")
    }
}
