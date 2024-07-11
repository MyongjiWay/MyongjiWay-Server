package com.myongjiway.error

class CoreException(
    val coreErrorType: CoreErrorType,
    val data: Any? = null,
) : RuntimeException(coreErrorType.message)
