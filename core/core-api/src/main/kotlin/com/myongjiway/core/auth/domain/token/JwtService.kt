package com.myongjiway.core.auth.domain.token

import com.myongjiway.core.auth.security.config.JwtProperty
import io.jsonwebtoken.Jwts
import jakarta.servlet.ServletRequest
import org.springframework.stereotype.Component

@Component
class JwtService(
    private val jwtProperty: JwtProperty,
) {
    fun generateTokenByUserId(userId: String, tokenType: TokenType): Token {
        val (expiration, secret)
    }
    fun validateAccessTokenFromRequest(servletRequest: ServletRequest, token: String?): Boolean {
        try {
            val claims = Jwts.parser().verifyWith()
        }
    }

    private fun getExpirationAndSecret(tokenType: TokenType) = when (tokenType) {
        TokenType.ACCESS -> jwtProperty.accesstoken.expiration to jwtProperty.accesstoken.secret
        TokenType.REFRESH -> jwtProperty.refreshtoken.expiration to jwtProperty.refreshtoken.secret
    }
}
