package com.myongjiway.web.security.web

import com.myongjiway.core.domain.token.TokenGenerator
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler(
    private val tokenGenerator: TokenGenerator,
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val user = authentication.principal as UserPrincipal

        val token = tokenGenerator.generateAccessTokenByUserId(user.getId().toString())

        val cookie = Cookie("Authorization", token.token)
        cookie.isHttpOnly = true
        cookie.path = "/"
        cookie.secure = true
        response.addCookie(cookie)

        redirectStrategy.sendRedirect(request, response, "/admin/home")
    }
}
