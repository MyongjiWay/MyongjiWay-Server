package com.myongjiway.client.kakao

import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@Component
@HttpExchange
internal interface KakaoApi {

    @PostExchange(
        url = "/v1/user/unlink",
        contentType = "application/x-www-form-urlencoded",
    )
    fun unlink(
        @RequestHeader("Authorization") adminKey: String,
        @RequestBody request: MultiValueMap<String, Any>,
    ): KakaoUnlinkResponse
}
