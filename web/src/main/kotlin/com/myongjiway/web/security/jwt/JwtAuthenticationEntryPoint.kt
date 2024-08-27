package com.myongjiway.web.security.jwt

import com.myongjiway.web.support.WebErrorType
import com.myongjiway.web.support.WebException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
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
        val errorType: WebErrorType

        when (exception) {
            "SignatureException" -> {
                errorType = WebErrorType.INVALID_TOKEN_ERROR
            }

            "ExpiredJwtException" -> {
                errorType = WebErrorType.EXPIRED_JWT_ERROR
                logger.error("만료된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "MalformedJwtException" -> {
                errorType = WebErrorType.INVALID_TOKEN_ERROR
                logger.error("잘못된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "HiJackException" -> {
                errorType = WebErrorType.HIJACK_JWT_TOKEN_ERROR
                logger.error("탈취된 토큰으로 접근했습니다. : {}", authException.message, authException)
            }

            "NoSuchElementException" -> {
                errorType = WebErrorType.INVALID_TOKEN_ERROR
                logger.error("존재하지 않는 유저입니다. : {}", authException.message, authException)
            }

            else -> {
                errorType = WebErrorType.UNAUTHORIZED_ERROR
                logger.error("로그인 후 이용가능합니다. 토큰을 입력해 주세요. : {}", authException.message, authException)
            }
        }
        val apiException = WebException(errorType)

        setResponse(response, apiException)
    }

    private fun setResponse(response: HttpServletResponse, exception: WebException) {
        response.contentType = "application/json;charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = exception.webErrorType.status.value()
        val json = JSONObject().apply {
            put("result", "ERROR")
            put("data", null)
            put(
                "error",
                JSONObject().apply {
                    put("code", exception.webErrorType.code)
                    put("message", exception.webErrorType.message)
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
