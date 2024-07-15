package com.myongjiway.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty

internal data class KakaoUnlinkRequest(
    @JsonProperty("target_id_type")
    val targetIdType: String,
    @JsonProperty("target_id")
    val targetId: Long,
)
