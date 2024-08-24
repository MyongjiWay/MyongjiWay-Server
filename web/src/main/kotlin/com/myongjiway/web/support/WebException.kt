package com.myongjiway.web.support

class WebException(
    val webErrorType: WebErrorType,
    val data: Any? = null,
) : RuntimeException(webErrorType.message)
