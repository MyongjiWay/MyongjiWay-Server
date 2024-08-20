package com.myongjiway.core.api.auth.security.domain

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.token.TokenValidator
import com.myongjiway.core.domain.user.UserRepository
import io.jsonwebtoken.security.Keys
import jakarta.servlet.ServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

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
        } catch (e: Exception) {
            servletRequest.setAttribute("exception", e.javaClass.simpleName)
        }
        return false
    }

    fun getAuthentication(token: String?): Authentication {
        val userId = try {
            tokenValidator.validateWithSecretKey(
                Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()),
                token!!,
            ).subject.toLong()
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
