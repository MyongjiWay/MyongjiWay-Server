package com.myongjiway.core.api.auth.security.domain

data class KakaoLoginData(
    val providerId: String,
    val username: String,
    val profileImg: String,
)
