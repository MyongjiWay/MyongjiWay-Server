package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
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
        val errorCode = ErrorType.NOT_ALLOWED_ACCESS_ERROR
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
        response.writer.print(json)
    }
}
