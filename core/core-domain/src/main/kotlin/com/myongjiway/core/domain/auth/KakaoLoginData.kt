package com.myongjiway.core.domain.auth

data class KakaoLoginData(
    var providerId: String,
    val username: String,
    val profileImg: String,
)
