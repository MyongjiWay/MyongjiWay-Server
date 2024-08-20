package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.slf4j.MDC
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
        MDC.put("userId", "anonymous")
        when (exception) {
            "NotExistUser" -> {
                setResponse(response, ErrorType.NOT_EXIST_USER_ERROR)
                logger.error("존재하지 않는 유저입니다. : {}", authException.message, authException)
            }

            "ExpiredJwtException" -> {
                setResponse(response, ErrorType.EXPIRED_JWT_ERROR)
                logger.error("만료된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "MalformedJwtException" -> {
                setResponse(response, ErrorType.INVALID_TOKEN_ERROR)
                logger.error("잘못된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "HiJackException" -> {
                setResponse(response, ErrorType.HIJACK_JWT_TOKEN_ERROR)
                logger.error("탈취된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            null -> {
                setResponse(response, ErrorType.UNAUTHORIZED_ERROR)
                logger.error("로그인 후 이용가능합니다. 토큰을 입력해 주세요. : {}", authException.message, authException)
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

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
