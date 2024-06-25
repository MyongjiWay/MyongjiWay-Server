package com.myongjiway.token

import java.util.Date

enum class TokenType {
    ACCESS,
    REFRESH,
    ;

    fun generate(expiration: Date, token: String, userId: String): Token = when (this) {
        ACCESS -> AccessToken(
            userId,
            token,
            expiration.time,
        )

        REFRESH -> RefreshToken(
            userId,
            token,
            expiration.time,
        )
    }
}
