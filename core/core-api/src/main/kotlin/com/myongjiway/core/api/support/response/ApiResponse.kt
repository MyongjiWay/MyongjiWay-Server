package com.myongjiway.core.api.support.response

import com.myongjiway.core.api.support.error.ErrorMessage
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.domain.error.CoreErrorType

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun success(): ApiResponse<Any> = ApiResponse(ResultType.SUCCESS, null, null)

        fun <S> success(data: S): ApiResponse<S> = ApiResponse(ResultType.SUCCESS, data, null)

        fun <S> error(error: ErrorType, errorData: Any? = null): ApiResponse<S> = ApiResponse(ResultType.ERROR, null, ErrorMessage(error, errorData))

        fun <S> error(error: CoreErrorType, errorData: Any? = null): ApiResponse<S> = ApiResponse(ResultType.ERROR, null, ErrorMessage(error, errorData))
    }
}
