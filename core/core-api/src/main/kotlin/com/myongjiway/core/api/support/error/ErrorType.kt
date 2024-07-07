@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.api.support.error

import com.myongjiway.core.api.support.error.ErrorCode.*
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    DEFAULT_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        E500,
        "An unexpected error has occurred.",
        LogLevel.ERROR,
    ),

    /**
     *  인증 관련 에러코드
     */
    FORBIDDEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH002, AUTH002.getDescription(), LogLevel.ERROR),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, AUTH003, AUTH003.getDescription(), LogLevel.ERROR),
    EXPIRED_JWT_ERROR(HttpStatus.UNAUTHORIZED, AUTH004, AUTH004.getDescription(), LogLevel.ERROR),
    RE_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, AUTH005, AUTH005.getDescription(), LogLevel.ERROR),
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH006, AUTH006.getDescription(), LogLevel.ERROR),
    HIJACK_JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH007, AUTH007.getDescription(), LogLevel.ERROR),
    INVALID_REFRESH_TOKEN_ERROR(HttpStatus.BAD_REQUEST, AUTH009, AUTH009.getDescription(), LogLevel.ERROR),
    EMPTY_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH010, AUTH010.name, LogLevel.ERROR),

    /**
     *  유저 관련 에러코드
     */
    NOT_EXIST_USER_ERROR(HttpStatus.UNAUTHORIZED, U009, U009.getDescription(), LogLevel.ERROR),
    NOT_ALLOWED_ACCESS_ERROR(HttpStatus.FORBIDDEN, U010, U010.getDescription(), LogLevel.ERROR),
}
