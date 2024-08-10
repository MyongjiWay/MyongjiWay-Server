package com.myongjiway.token

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtProperty {

    @Value("\${jwt.access-token}")
    lateinit var accessToken: TokenProperties

    @Value("\${jwt.refresh-token}")
    lateinit var refreshToken: TokenProperties

    class TokenProperties {
        lateinit var secret: String
        var expiration: Long = 0
    }
}
