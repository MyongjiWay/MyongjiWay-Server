package com.myongjiway.client.apple

import com.myongjiway.client.apple.model.ApplePublicKeyResult
import org.springframework.stereotype.Component
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@Component
@HttpExchange
interface AppleApi {

    @GetExchange(
        url = "/auth/keys",
    )
    fun getPublicKey(): ApplePublicKeyResult
}
