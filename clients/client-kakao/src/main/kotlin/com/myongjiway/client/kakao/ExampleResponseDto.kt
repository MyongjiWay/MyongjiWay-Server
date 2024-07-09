package com.myongjiway.client.kakao

import com.myongjiway.client.kakao.model.ExampleClientResult

internal data class ExampleResponseDto(
    val exampleResponseValue: String,
) {
    fun toResult(): ExampleClientResult = ExampleClientResult(exampleResponseValue)
}
