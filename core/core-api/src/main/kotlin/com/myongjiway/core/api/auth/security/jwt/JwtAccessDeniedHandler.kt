package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val errorCode = ErrorType.NOT_ALLOWED_ACCESS_ERROR
        response.contentType = "application/json;charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val json = JSONObject().apply {
            put("code", errorCode.code)
            put("message", errorCode.message)
            put("isSuccess", false)
        }

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated) {
            val userString = authentication.name.toString()
            val userId = parseUserIdFromPrincipal(userString)
            MDC.put("userId", userId)
        }

        logger.error("접근 권한이 없는 토큰으로 요청했습니다. : {}", accessDeniedException.message, accessDeniedException)

        response.writer.print(json)
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
