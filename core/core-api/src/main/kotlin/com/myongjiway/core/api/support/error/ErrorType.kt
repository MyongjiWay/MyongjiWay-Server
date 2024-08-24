@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.api.support.error

import com.myongjiway.core.api.support.error.ErrorCode.*
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    /**
     *  기본 에러코드
     */
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", LogLevel.ERROR),
    COMMON_ERROR(HttpStatus.BAD_REQUEST, C400, "요청이 잘못되었습니다.", LogLevel.ERROR),
}
