package com.myongjiway.core.auth.security.jwt

data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
)
