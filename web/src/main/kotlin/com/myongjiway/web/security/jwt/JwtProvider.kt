package com.myongjiway.web.security.jwt

import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.token.TokenValidator
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserReader
import io.jsonwebtoken.security.Keys
import jakarta.servlet.ServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    private val jwtProperty: JwtProperty,
    private val tokenValidator: TokenValidator,
    private val userReader: UserReader,
) {
    fun validateAccessTokenFromRequest(servletRequest: ServletRequest, token: String?): Boolean {
        try {
            tokenValidator.validate(
                Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()),
                token!!,
            ).subject.toLong()
            return true
        } catch (e: Exception) {
            servletRequest.setAttribute("exception", e.javaClass.simpleName)
        }
        return false
    }

    fun getAuthentication(servletRequest: ServletRequest, token: String?): Authentication {
        var user: User? = null
        try {
            val userId =
                tokenValidator.validate(
                    Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()),
                    token!!,
                ).subject.toLong()
            user = userReader.find(userId)
        } catch (e: Exception) {
            servletRequest.setAttribute("exception", e.javaClass.simpleName)
        }
        return UsernamePasswordAuthenticationToken(user, "", listOf(GrantedAuthority { user?.role?.value }))
    }
}
