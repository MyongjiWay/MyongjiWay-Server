package com.myongjiway.client.kakao

import com.myongjiway.client.kakao.model.ExampleClientResult
import org.springframework.stereotype.Component

@Component
class ExampleClient internal constructor(
    private val exampleApi: ExampleApi,
) {
    fun example(exampleParameter: String): ExampleClientResult {
        val request = ExampleRequestDto(exampleParameter)
        return exampleApi.example(request).toResult()
    }
}
