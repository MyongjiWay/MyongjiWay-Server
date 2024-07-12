package com.myongjiway.core.auth.security.domain

data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
)
