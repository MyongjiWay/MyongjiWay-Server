package com.myongjiway.core.domain.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.HS512
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class TokenGenerator(
    private val jwtProperty: JwtProperty,
) {
    fun generateAccessTokenByUserId(userId: String): Token {
        val (expiration, secret) = getExpirationAndSecret(TokenType.ACCESS)
        val expirationDate = Date(System.currentTimeMillis() + expiration)
        return TokenType.ACCESS.generate(
            token = Jwts.builder()
                .subject(userId)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()), HS512)
                .compact(),
            expiration = expirationDate,
            userId = userId,
        )
    }

    fun generateRefreshTokenByUserId(userId: String): Token {
        val (expiration, secret) = getExpirationAndSecret(TokenType.REFRESH)
        val expirationDate = Date(System.currentTimeMillis() + expiration)
        return TokenType.REFRESH.generate(
            token = Jwts.builder()
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()), HS512)
                .compact(),
            expiration = expirationDate,
            userId = userId,
        )
    }

    private fun getExpirationAndSecret(tokenType: TokenType) = when (tokenType) {
        TokenType.ACCESS -> jwtProperty.accessToken.expiration to jwtProperty.accessToken.secret
        TokenType.REFRESH -> jwtProperty.refreshToken.expiration to jwtProperty.refreshToken.secret
    }
}
