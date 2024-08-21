package com.myongjiway.client.apple

import com.myongjiway.client.apple.model.ApplePublicKeyResult
import org.springframework.stereotype.Component

@Component
class AppleClient internal constructor(
    private val appleAuthApi: AppleAuthApi,
) {
    fun getPublicKey(): ApplePublicKeyResult = appleAuthApi.getPublicKey()
}
