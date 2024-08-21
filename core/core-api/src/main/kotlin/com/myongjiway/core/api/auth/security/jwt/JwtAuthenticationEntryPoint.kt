package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.support.error.CoreApiException
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
        val errorType: ErrorType
        MDC.put("userId", "anonymous")
        when (exception) {
            "NotExistUser" -> {
                errorType = ErrorType.NOT_EXIST_USER_ERROR
                logger.error("존재하지 않는 유저입니다. : {}", authException.message, authException)
            }

            "SignatureException" -> {
                errorType = ErrorType.INVALID_TOKEN_ERROR
            }

            "ExpiredJwtException" -> {
                errorType = ErrorType.EXPIRED_JWT_ERROR
                logger.error("만료된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "MalformedJwtException" -> {
                errorType = ErrorType.INVALID_TOKEN_ERROR
                logger.error("잘못된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "HiJackException" -> {
                errorType = ErrorType.HIJACK_JWT_TOKEN_ERROR
                logger.error("탈취된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "NoSuchElementException" -> {
                errorType = ErrorType.NOT_EXIST_USER_ERROR
                logger.error("존재하지 않는 유저입니다. : {}", authException.message, authException)
            }

            else -> {
                errorType = ErrorType.UNAUTHORIZED_ERROR
                logger.error("로그인 후 이용가능합니다. 토큰을 입력해 주세요. : {}", authException.message, authException)
            }
        }
        val apiException = CoreApiException(errorType)

        setResponse(response, apiException)
    }

    private fun setResponse(response: HttpServletResponse, exception: CoreApiException) {
        response.contentType = "application/json;charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = exception.errorType.status.value()
        val json = JSONObject().apply {
            put("result", "ERROR")
            put("data", null)
            put(
                "error",
                JSONObject().apply {
                    put("code", exception.errorType.code)
                    put("message", exception.errorType.message)
                    put("data", exception.data)
                },
            )
        }
        response.writer.print(json)
    }

    companion object {
        private val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
