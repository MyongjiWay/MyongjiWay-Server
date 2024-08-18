package com.myongjiway.core.api.auth.security.domain

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.token.TokenValidator
import com.myongjiway.core.domain.user.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
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

@Component
class JwtProvider(
    private val jwtProperty: JwtProperty,
    private val tokenValidator: TokenValidator,
    private val userRepository: UserRepository,
) {
    fun validateAccessTokenFromRequest(servletRequest: ServletRequest, token: String?): Boolean {
        try {
            tokenValidator.validateWithSecretKey(
                Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()),
                token!!,
            )
            return true
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

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
