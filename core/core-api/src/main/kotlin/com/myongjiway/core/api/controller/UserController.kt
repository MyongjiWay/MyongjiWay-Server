package com.myongjiway.core.api.controller

import com.myongjiway.client.kakao.KakaoClient
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.user.User
import com.myongjiway.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val kakaoClient: KakaoClient,
) {

    @PostMapping("/inactive")
    fun inactive(
        @AuthenticationPrincipal user: User,
    ): ApiResponse<Any> {
        val kakaoClientResult = kakaoClient.unlink(user.providerId)
        userService.inactive(kakaoClientResult.id)
        return ApiResponse.success()
    }
}
