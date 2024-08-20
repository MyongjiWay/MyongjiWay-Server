package com.myongjiway.core.api.controller

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.error.CoreException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(CoreApiException::class)
    fun handleCoreApiException(e: CoreApiException): ResponseEntity<ApiResponse<Any>> {
        MDC.put("status", e.errorType.status.toString())
        logging(e)
        return ResponseEntity(ApiResponse.error(e.errorType, e.data), e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        MDC.put("status", "500")
        logging(e, true)
        return ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR, e.message), ErrorType.DEFAULT_ERROR.status)
    }

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        MDC.put("status", "400")
        logging(e)
        return ResponseEntity(ApiResponse.error(e.coreErrorType, e.data), ErrorType.COMMON_ERROR.status)
    }

    private fun logging(e: Throwable, includeFullStackTrace: Boolean = false) {
        if (includeFullStackTrace) {
            MDC.put("exceptionFull", e.stackTraceToString())
        }
        logger.error("Exception occurred", e)
        MDC.clear()
    }

    companion object {
        private val logger = LoggerFactory.getLogger("ErrorLog")
    }
}
