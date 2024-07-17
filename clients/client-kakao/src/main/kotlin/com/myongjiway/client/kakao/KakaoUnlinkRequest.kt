package com.myongjiway.client.kakao

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

internal data class KakaoUnlinkRequest(
    val targetIdType: String,
    val targetId: Long,
) {
    fun toBody(): MultiValueMap<String, Any> = LinkedMultiValueMap<String, Any>().apply {
        add("target_id_type", targetIdType)
        add("target_id", targetId)
    }
}
