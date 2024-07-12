package com.myongjiway.core.auth.controller

import com.myongjiway.core.auth.controller.v1.request.KakaoLoginRequest
import com.myongjiway.core.auth.security.jwt.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {
    fun kakaoLogin(
        @Valid @RequestBody request: KakaoLoginRequest,
    ) {
        authService.kakaoLogin(request.toKakaoLoginData())
    }
}
