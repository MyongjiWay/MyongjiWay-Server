package com.myongjiway.core.auth.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.filter.GenericFilterBean

@Component
class JwtFilter(
    private val jwtService: JwtService,
) : GenericFilterBean() {
    override fun doFilter(
        servletRequest: ServletRequest?,
        servletResponse: ServletResponse?,
        filterChain: FilterChain?,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = getJwt()
        if (jwt.isNullOrBlank() && jwtService.validateAccessTokenFromRequest(servletRequest, jwt)) {
        } else {
            (servletResponse as jakarta.servlet.http.HttpServletResponse).sendError(401)
        }
    }

    private fun getJwt(): String? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader("Authorization")
    }
}
