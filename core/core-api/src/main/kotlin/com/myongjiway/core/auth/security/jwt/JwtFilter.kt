package com.myongjiway.core.auth.security.jwt

import com.myongjiway.core.auth.security.config.SecurityConstants
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

        if (isExcludedUrl(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }

        if (!jwt.isNullOrBlank() && jwtProvider.validateAccessTokenFromRequest(servletRequest, jwt)) {
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
        val authHeader = request.getHeader("Authorization")
        return authHeader?.let {
            if (it.startsWith("Bearer ")) {
                it.substring(7) // "Bearer " 이후의 문자열 반환
            } else {
                null
            }
        }
    }

    private fun isExcludedUrl(uri: String): Boolean = SecurityConstants.EXCLUDED_URLS.any { regex -> uri.matches(regex) }

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
