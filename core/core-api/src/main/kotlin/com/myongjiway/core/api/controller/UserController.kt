package com.myongjiway.core.api.controller

import com.myongjiway.client.kakao.KakaoClient
import com.myongjiway.core.api.controller.v1.response.UserInactiveResponseDto
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
class UserController(
    private val userService: UserService,
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
}
