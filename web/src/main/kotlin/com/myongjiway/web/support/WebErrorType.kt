@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.web.support

import com.myongjiway.web.support.WebErrorCode.*
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class WebErrorType(val status: HttpStatus, val code: WebErrorCode, val message: String, val logLevel: LogLevel) {

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
    NOT_ALLOWED_ACCESS_ERROR(HttpStatus.FORBIDDEN, AUTH011, "접근 권한이 없습니다.", LogLevel.ERROR),
}
