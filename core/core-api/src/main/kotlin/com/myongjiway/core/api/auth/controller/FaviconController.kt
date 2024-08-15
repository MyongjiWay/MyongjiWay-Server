package com.myongjiway.core.api.auth.controller

import com.myongjiway.core.api.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FaviconController {
    @GetMapping("/favicon.ico")
    fun favicon(): ApiResponse<Any> = ApiResponse.success()
}
