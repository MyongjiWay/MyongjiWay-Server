package com.myongjiway.client.kakao

import com.myongjiway.client.kakao.model.KakaoUnlinkResult
import org.springframework.stereotype.Component

@Component
class KakaoClient internal constructor(
    private val kakaoApi: KakaoApi,
    private val kakaoRestClientConfig: KakaoRestClientConfig,
) {
    fun unlink(providerId: String): KakaoUnlinkResult {
        val response = kakaoApi.unlink(
            "KakaoAK " + kakaoRestClientConfig.adminKey,
            KakaoUnlinkRequest("user_id", providerId.toLong()).toBody(),
        )
        return KakaoUnlinkResult(response.id)
    }
}
