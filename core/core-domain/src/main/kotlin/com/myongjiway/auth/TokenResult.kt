package com.myongjiway.auth

data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
)
