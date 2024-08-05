package com.myongjiway.core.auth.security.jwt

import com.myongjiway.core.auth.security.domain.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.filter.GenericFilterBean

@Component
class JwtFilter(
    private val jwtProvider: JwtProvider,
) : GenericFilterBean() {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = getJwt()
        val requestURI = httpServletRequest.requestURI
        if (jwt.isNullOrBlank() && jwtProvider.validateAccessTokenFromRequest(servletRequest, jwt)) {
            val authentication = jwtProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            Companion.logger.info("Security Context에 '${authentication.name}' 인증 정보를 저장했습니다. uri: $requestURI")
        } else {
            Companion.logger.info("유효한 JWT 토큰이 없습니다. uri: $requestURI")
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getJwt(): String? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader("Authorization")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtFilter::class.java)
    }
}
