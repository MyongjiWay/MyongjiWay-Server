package com.myongjiway.core.api.auth.controller.v1.request

import com.myongjiway.core.api.auth.security.domain.KakaoLoginData
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class KakaoLoginRequest(

    @field:NotBlank
    val providerId: String,
    @field:NotNull
    val username: String,
    val profileImg: String,
) {
    fun toKakaoLoginData(): KakaoLoginData = KakaoLoginData(
        providerId = providerId,
        username = username,
        profileImg = profileImg,
    )
}
