package com.myongjiway.token

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtProperty {

    lateinit var accessToken: TokenProperties
    lateinit var refreshToken: TokenProperties

    class TokenProperties {
        lateinit var secret: String
        var expiration: Long = 0
    }
}
