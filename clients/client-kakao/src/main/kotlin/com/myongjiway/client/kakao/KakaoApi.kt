package com.myongjiway.client.kakao

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@Component
@HttpExchange
interface KakaoApi {

    @PostExchange("/v1/user/unlink")
    fun unlink(
        @RequestHeader("Authorization") adminKey: String,
        @RequestBody request: KakaoUnlinkRequest,
    ): KakaoUnlinkResponse
}
