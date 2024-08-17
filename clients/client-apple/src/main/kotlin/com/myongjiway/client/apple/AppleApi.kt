package com.myongjiway.client.apple

import com.myongjiway.client.apple.model.ApplePublicKeyResult
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@Component
@HttpExchange
interface AppleApi {

    @GetExchange(
        url = "/auth/keys",
    )
    fun getIdentityToken(
        @RequestHeader("Authorization") adminKey: String,
        @RequestBody request: MultiValueMap<String, Any>,
    ): ApplePublicKeyResult
}
