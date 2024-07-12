package com.myongjiway.core.auth.security.jwt

data class KakaoLoginData(
    val providerId: String,
    val username: String,
    val profileImg: String,
)
