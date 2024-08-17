package com.myongjiway.core.domain.token

import java.util.Date

enum class TokenType {
    ACCESS,
    REFRESH,
    APPLE,
    ;

    fun generate(expiration: Date, token: String, userId: String): Token = when (this) {
        ACCESS -> Token(
            userId,
            token,
            expiration.time,
            ACCESS,
        )

        REFRESH -> Token(
            userId,
            token,
            expiration.time,
            REFRESH,
        )

        APPLE -> TODO()
    }
}
