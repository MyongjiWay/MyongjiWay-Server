@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.auth.security.domain

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.token.Token
import com.myongjiway.token.TokenType
import com.myongjiway.token.TokenType.*
import com.myongjiway.user.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.*
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import jakarta.servlet.ServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.Date

@Component
class JwtProvider(
    private val jwtProperty: JwtProperty,
    private val userRepository: UserRepository,
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

    fun validateAccessTokenFromRequest(servletRequest: ServletRequest, token: String?): Boolean {
        try {
            val claims =
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray())).build()
                    .parseSignedClaims(token).payload
            val expirationDate = claims.expiration
            return !expirationDate.before(Date())
        } catch (e: SecurityException) {
            servletRequest.setAttribute("exception", "MalformedJwtException")
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            servletRequest.setAttribute("exception", "MalformedJwtException")
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            servletRequest.setAttribute("exception", "ExpiredJwtException")
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            servletRequest.setAttribute("exception", "UnsupportedJwtException")
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            servletRequest.setAttribute("exception", "IllegalArgumentException")
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    fun getAuthentication(token: String?): Authentication {
        val userId = try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray())).build()
                .parseSignedClaims(token).payload.subject.toLong()
        } catch (e: Exception) {
            throw CoreApiException(ErrorType.INVALID_TOKEN_ERROR)
        }
        val user = userRepository.findUserById(userId)
        return UsernamePasswordAuthenticationToken(user, "", listOf(GrantedAuthority { user?.role?.value }))
    }

    private fun getExpirationAndSecret(tokenType: TokenType) = when (tokenType) {
        ACCESS -> jwtProperty.accessToken.expiration to jwtProperty.accessToken.secret
        REFRESH -> jwtProperty.refreshToken.expiration to jwtProperty.refreshToken.secret
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtProvider::class.java)
    }
}
