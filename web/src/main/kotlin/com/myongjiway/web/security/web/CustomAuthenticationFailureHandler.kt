package com.myongjiway.web.security.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class CustomAuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        val errorMessage: String = when (exception) {
            is BadCredentialsException -> "Invalid username or password"
            is DisabledException -> "Your account has been disabled"
            is LockedException -> "Your account has been locked"
            is CredentialsExpiredException -> "Your credentials have expired"
            else -> exception.message ?: "Authentication failed"
        }

        redirectStrategy.sendRedirect(
            request,
            response,
            "/login?error=true&message=" + URLEncoder.encode(errorMessage, "UTF-8"),
        )
    }
}
