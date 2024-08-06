package com.myongjiway.core.auth.controller

import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.auth.controller.v1.request.KakaoLoginRequest
import com.myongjiway.core.auth.security.domain.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/kakao-login")
    fun kakaoLogin(
        @Valid @RequestBody request: KakaoLoginRequest,
    ): ApiResponse<Any> {
        val result = authService.kakaoLogin(request.toKakaoLoginData())
        return ApiResponse.success(result)
    }
}
