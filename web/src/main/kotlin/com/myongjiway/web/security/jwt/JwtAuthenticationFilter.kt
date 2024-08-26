package com.myongjiway.web.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val jwt = getJwt(servletRequest)
        val requestURI = servletRequest.requestURI
        if (!jwt.isNullOrBlank() && jwtProvider.validateAccessTokenFromRequest(servletRequest, jwt)) {
            val authentication = jwtProvider.getAuthentication(servletRequest, jwt)
            if (authentication.principal != null) {
                SecurityContextHolder.getContext().authentication = authentication
                setMDC(servletRequest, parseUserIdFromPrincipal(authentication.principal.toString()))
                Companion.logger.info("Security Context에 '${authentication.name}' 인증 정보를 저장했습니다. uri: $requestURI")
            }
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun setMDC(request: HttpServletRequest, userId: String) {
        MDC.put("userId", userId)
        MDC.put("sourceIp", request.getHeader("X-Real-IP") ?: request.remoteAddr)
        MDC.put("xForwardedFor", request.getHeader("X-Forwarded-For"))
    }

    private fun getJwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7)
        }

        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "Authorization") {
                    return cookie.value
                }
            }
        }

        return null
    }

    private fun parseUserIdFromPrincipal(userString: String): String {
        val idPattern = """id=(\d+)""".toRegex()
        val matchResult = idPattern.find(userString)
        return matchResult?.groupValues?.get(1) ?: "unknown"
    }

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
