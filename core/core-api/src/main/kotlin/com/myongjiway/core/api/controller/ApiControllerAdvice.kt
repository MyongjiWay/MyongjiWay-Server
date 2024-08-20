package com.myongjiway.core.api.controller

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.error.CoreException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(CoreApiException::class)
    fun handleCoreApiException(e: CoreApiException): ResponseEntity<ApiResponse<Any>> = ResponseEntity(ApiResponse.error(e.errorType, e.data), e.errorType.status)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> = ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR, e.message), ErrorType.DEFAULT_ERROR.status)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> = ResponseEntity(ApiResponse.error(e.coreErrorType, e.data), ErrorType.COMMON_ERROR.status)
}
