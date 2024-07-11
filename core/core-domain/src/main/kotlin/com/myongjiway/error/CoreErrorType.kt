package com.myongjiway.error

enum class CoreErrorType(
    val kind: CoreErrorKind,
    val code: CoreErrorCode,
    val message: String,
    val level: CoreErrorLevel,
) {
    NOT_FOUND_DATA(CoreErrorKind.SERVER_ERROR, CoreErrorCode.E1000, "해당 데이터를 찾지 못했습니다.", CoreErrorLevel.INFO),
}
