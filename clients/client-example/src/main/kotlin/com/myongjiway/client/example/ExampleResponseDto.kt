package com.myongjiway.client.example

import com.myongjiway.client.example.model.ExampleClientResult

internal data class ExampleResponseDto(
    val exampleResponseValue: String,
) {
    fun toResult(): ExampleClientResult {
        return ExampleClientResult(exampleResponseValue)
    }
}
