package com.myongjiway.core.auth.controller

import com.myongjiway.auth.AuthService
import com.myongjiway.core.auth.controller.v1.request.KakaoLoginRequest
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
