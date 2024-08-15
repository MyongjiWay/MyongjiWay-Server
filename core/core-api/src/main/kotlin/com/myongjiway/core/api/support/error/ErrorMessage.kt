package com.myongjiway.core.api.support.error

import com.myongjiway.core.domain.error.CoreErrorType

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorType: ErrorType, data: Any? = null) : this(
        code = errorType.code.name,
        message = errorType.message,
        data = data,
    )

    constructor(coreErrorType: CoreErrorType, data: Any? = null) : this(
        code = coreErrorType.code.name,
        message = coreErrorType.message,
        data = data,
    )
}
