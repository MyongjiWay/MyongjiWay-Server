package com.myongjiway.core.domain.token

interface Token {
    val userId: String
    val token: String
    val expiration: Long
}

data class AccessToken(
    override val userId: String,
    override val token: String,
    override val expiration: Long,
) : Token

data class RefreshToken(
    override val userId: String,
    override val token: String,
    override val expiration: Long,
) : Token
