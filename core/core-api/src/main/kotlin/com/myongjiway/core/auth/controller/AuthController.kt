package com.myongjiway.core.auth.controller

import com.myongjiway.auth.AuthService
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {
    fun kakaoLogin() {
    }
}
