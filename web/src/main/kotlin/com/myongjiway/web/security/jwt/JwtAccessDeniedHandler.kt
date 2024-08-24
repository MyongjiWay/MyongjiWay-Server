package com.myongjiway.web.security.jwt

import com.myongjiway.web.support.WebErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val errorCode = WebErrorType.NOT_ALLOWED_ACCESS_ERROR
        response.contentType = "application/json;charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = errorCode.status.value()
        val json = JSONObject().apply {
            put("result", "ERROR")
            put("data", null) // Always null
            put(
                "error",
                JSONObject().apply {
                    put("code", errorCode.code)
                    put("message", errorCode.message)
                    put("data", null) // No additional data in this context
                },
            )
        }

        logger.error("접근 권한이 없는 토큰으로 요청했습니다. : {}", accessDeniedException.message, accessDeniedException)

        response.writer.print(json)
    }

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
