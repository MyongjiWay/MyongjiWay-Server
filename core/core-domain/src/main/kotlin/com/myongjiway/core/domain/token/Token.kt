package com.myongjiway.core.domain.token

data class Token(
    val userId: String,
    val token: String,
    val expiration: Long,
    val tokenType: TokenType,
)
