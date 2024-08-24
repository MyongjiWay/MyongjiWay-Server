package com.myongjiway.core.api.auth.controller

import com.myongjiway.core.api.auth.controller.v1.request.KakaoLoginRequest
import com.myongjiway.core.api.controller.v1.request.RefreshRequest
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.auth.AuthService
import com.myongjiway.core.domain.token.RefreshData
import com.myongjiway.core.domain.token.TokenService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService,
    private val tokenService: TokenService,
) {
    @PostMapping("/kakao-login")
    fun kakaoLogin(
        @Valid @RequestBody request: KakaoLoginRequest,
    ): ApiResponse<Any> {
        val result = authService.kakaoLogin(request.toKakaoLoginData())
        return ApiResponse.success(result)
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody request: RefreshRequest,
    ): ApiResponse<Any> {
        val result = tokenService.refresh(RefreshData(request.refreshToken))
        return ApiResponse.success(result)
    }

    @DeleteMapping("/logout")
    fun logout(
        @Valid @RequestBody request: RefreshRequest,
    ): ApiResponse<Any> {
        tokenService.delete(request.refreshToken)
        return ApiResponse.success()
    }
}
