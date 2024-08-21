package com.myongjiway.core.api.auth.security.jwt

import com.myongjiway.core.api.support.error.CoreApiException
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
        val errorType = when (exception) {
            "NotExistUser" -> ErrorType.NOT_EXIST_USER_ERROR
            "SignatureException" -> ErrorType.INVALID_TOKEN_ERROR
            "ExpiredJwtException" -> ErrorType.EXPIRED_JWT_ERROR
            "MalformedJwtException" -> ErrorType.INVALID_TOKEN_ERROR
            "HiJackException" -> ErrorType.HIJACK_JWT_TOKEN_ERROR
            "NoSuchElementException" -> ErrorType.NOT_EXIST_USER_ERROR
            else -> ErrorType.UNAUTHORIZED_ERROR
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
}
