package com.myongjiway.core.api.controller

import com.myongjiway.client.kakao.KakaoClient
import com.myongjiway.core.api.controller.v1.request.RefreshRequest
import com.myongjiway.core.api.controller.v1.response.UserInactiveResponseDto
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.token.RefreshData
import com.myongjiway.token.TokenService
import com.myongjiway.user.User
import com.myongjiway.user.UserService
import jakarta.servlet.ServletRequest
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val kakaoClient: KakaoClient,
) {
    @PatchMapping("/inactive")
    fun inactive(
        @AuthenticationPrincipal user: User,
    ): ApiResponse<Any> {
        val kakaoClientResult = kakaoClient.unlink(user.providerId)
        val inactiveUserId = userService.inactive(kakaoClientResult.id.toString())
        return ApiResponse.success(UserInactiveResponseDto(inactiveUserId))
    }

    @PostMapping("/refresh")
    fun refresh(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: RefreshRequest,
        servletRequest: ServletRequest,
    ): ApiResponse<Any> {
        val result = tokenService.refresh(RefreshData(user.id, request.refreshToken))
        return ApiResponse.success(result)
    }
}
