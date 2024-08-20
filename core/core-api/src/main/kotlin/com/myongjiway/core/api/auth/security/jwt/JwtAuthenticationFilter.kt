package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.auth.security.domain.JwtValidator
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtValidator: JwtValidator,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = getJwt()
        val requestURI = httpServletRequest.requestURI
        if (!jwt.isNullOrBlank() && jwtValidator.validateAccessTokenFromRequest(servletRequest, jwt)) {
            val authentication = jwtValidator.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            MDC.put("userId", parseUserIdFromPrincipal(authentication.name.toString()))
            Companion.logger.info("${authentication.name} 해당하는 유저가 $requestURI 경로로 접근했습니다.")
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getJwt(): String? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val authHeader = request.getHeader("Authorization")
        return authHeader?.let {
            if (it.startsWith("Bearer ")) {
                it.substring(7)
            } else {
                null
            }
        }
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
