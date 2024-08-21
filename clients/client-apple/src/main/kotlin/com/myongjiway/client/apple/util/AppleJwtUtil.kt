package com.myongjiway.client.apple.util

import com.myongjiway.client.apple.AppleClient
import com.myongjiway.core.domain.token.TokenValidator

class AppleJwtUtil(
    private val tokenValidator: TokenValidator,
    private val appleClient: AppleClient,
)
