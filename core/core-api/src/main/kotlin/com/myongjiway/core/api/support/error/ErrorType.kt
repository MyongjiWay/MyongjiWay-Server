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
    FORBIDDEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH002, "해당 요청에 대한 권한이 없습니다.", LogLevel.ERROR),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, AUTH003, "로그인 후 이용가능합니다. 토큰을 입력해 주세요.", LogLevel.ERROR),
    EXPIRED_JWT_ERROR(HttpStatus.UNAUTHORIZED, AUTH004, "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요.", LogLevel.ERROR),
    RE_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, AUTH005, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", LogLevel.ERROR),
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH006, "토큰이 올바르지 않습니다.", LogLevel.ERROR),
    HIJACK_JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH007, "탈취된(로그아웃 된) 토큰입니다 다시 로그인 해주세요.", LogLevel.ERROR),
    INVALID_REFRESH_TOKEN_ERROR(HttpStatus.BAD_REQUEST, AUTH009, "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요.", LogLevel.ERROR),
    EMPTY_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, AUTH010, "토큰이 비어있습니다. 토큰을 보내주세요.", LogLevel.ERROR),

    /**
     *  유저 관련 에러코드
     */
    NOT_EXIST_USER_ERROR(HttpStatus.UNAUTHORIZED, U009, "해당 유저가 존재하지 않습니다.", LogLevel.ERROR),
    NOT_ALLOWED_ACCESS_ERROR(HttpStatus.FORBIDDEN, U010, "접근 권한이 없습니다.", LogLevel.ERROR),
}
