package com.myongjiway.core.auth.security.jwt

import com.myongjiway.core.api.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val exception = request.getAttribute("exception") as? String
        when (exception) {
            "NotExistUser" -> {
                setResponse(response, ErrorType.NOT_EXIST_USER_ERROR)
            }

            "ExpiredJwtException" -> {
                setResponse(response, ErrorType.EXPIRED_JWT_ERROR)
            }

            "MalformedJwtException" -> {
                setResponse(response, ErrorType.INVALID_TOKEN_ERROR)
            }

            "HiJackException" -> {
                setResponse(response, ErrorType.HIJACK_JWT_TOKEN_ERROR)
            }

            null -> {
                setResponse(response, ErrorType.UNAUTHORIZED_ERROR)
            }
        }
    }

    private fun setResponse(response: HttpServletResponse, errorType: ErrorType) {
        response.contentType = "application/json;charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val json = JSONObject().apply {
            put("code", errorType.code)
            put("message", errorType.message)
            put("isSuccess", false)
        }
        response.writer.print(json)
    }
}
