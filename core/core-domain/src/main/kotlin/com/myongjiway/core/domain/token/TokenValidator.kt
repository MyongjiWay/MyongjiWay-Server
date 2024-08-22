package com.myongjiway.core.domain.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PublicKey
import javax.crypto.SecretKey

@Component
class TokenValidator {
    fun validate(key: PublicKey, token: String): Claims =
        validateToken { Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload }

    fun validate(key: SecretKey, token: String): Claims =
        validateToken { Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload }

    fun validate(token: String): Claims =
        validateToken { Jwts.parser().build().parseSignedClaims(token).payload }

    private inline fun validateToken(validation: () -> Claims): Claims = try {
        validation()
    } catch (e: Exception) {
        throw e
    }
}
