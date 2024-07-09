package com.myongjiway.core.auth.controller.v1.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class KakaoLoginRequest(

    @field:NotBlank
    val providerId: String,
    @field:NotNull
    val username: String,
    val profileImg: String,
)
