package com.myongjiway.core.auth.security.token.service

import com.myongjiway.core.auth.security.config.JwtProperty
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SecretKeyBuilder
import jakarta.servlet.ServletRequest
import org.springframework.stereotype.Component

@Component
class JwtService(
    private val jwtProperty: JwtProperty,
) {
    fun validateAccessTokenFromRequest(servletRequest: ServletRequest, token: String?): Boolean {
        try {
            val claims = Jwts.parser().verifyWith()
        }
    }
}
