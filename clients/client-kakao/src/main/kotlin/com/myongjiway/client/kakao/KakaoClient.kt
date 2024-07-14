package com.myongjiway.client.kakao

import org.springframework.stereotype.Component

@Component
class KakaoClient internal constructor(
    private val kakaoApi: KakaoApi,
) {
    fun unlink(providerId: String): Long {
        val response = kakaoApi.unlink("admin_key", KakaoUnlinkRequest("user_id", providerId.toLong()))
        return response.id
    }
}
