package com.myongjiway.token

import com.myongjiway.token.TokenType.ACCESS
import com.myongjiway.token.TokenType.REFRESH
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
        val (expiration, secret) = getExpirationAndSecret(ACCESS)
        val expirationDate = Date(System.currentTimeMillis() + expiration)
        return ACCESS.generate(
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
        val (expiration, secret) = getExpirationAndSecret(REFRESH)
        val expirationDate = Date(System.currentTimeMillis() + expiration)
        return REFRESH.generate(
            token = Jwts.builder()
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()), HS512)
                .compact(),
            expiration = expirationDate,
            userId = userId,
        )
    }

    private fun getExpirationAndSecret(tokenType: TokenType) = when (tokenType) {
        ACCESS -> jwtProperty.accessToken.expiration to jwtProperty.accessToken.secret
        REFRESH -> jwtProperty.refreshToken.expiration to jwtProperty.refreshToken.secret
    }
}
