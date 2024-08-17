package com.myongjiway.client.apple

import com.myongjiway.client.apple.model.ApplePublicKeyResult
import org.springframework.stereotype.Component

@Component
class AppleClient internal constructor(
    private val appleApi: AppleApi,
) {
    fun getPublicKey(): ApplePublicKeyResult = appleApi.getPublicKey()
}
