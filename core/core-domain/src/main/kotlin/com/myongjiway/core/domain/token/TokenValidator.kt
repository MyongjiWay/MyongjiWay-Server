package com.myongjiway.core.domain.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.security.PublicKey
import javax.crypto.SecretKey

@Component
class TokenValidator {
    fun validateWithPublicKey(key: PublicKey, token: String): Claims = try {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload
    } catch (e: Exception) {
        handleJwtException(e)
    }

    fun validateWithSecretKey(key: SecretKey, token: String): Claims = try {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload
    } catch (e: Exception) {
        handleJwtException(e)
    }

    private fun handleJwtException(e: Exception): Nothing {
        when (e) {
            is SecurityException, is MalformedJwtException -> {
                logger.info("잘못된 JWT 서명입니다.")
                throw e
            }

            is ExpiredJwtException -> {
                logger.info("만료된 JWT 토큰입니다.")
                throw e
            }

            is UnsupportedJwtException -> {
                logger.info("지원되지 않는 JWT 토큰입니다.")
                throw e
            }

            is IllegalArgumentException -> {
                logger.info("JWT 토큰이 잘못되었습니다.")
                throw e
            }

            else -> throw e
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger("com.myongjiway.core.domain.token.TokenValidator")
    }
}
