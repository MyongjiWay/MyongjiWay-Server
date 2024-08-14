package com.myongjiway.core.domain.error

class CoreException(
    val coreErrorType: com.myongjiway.core.domain.error.CoreErrorType,
    val data: Any? = null,
) : RuntimeException(coreErrorType.message)
