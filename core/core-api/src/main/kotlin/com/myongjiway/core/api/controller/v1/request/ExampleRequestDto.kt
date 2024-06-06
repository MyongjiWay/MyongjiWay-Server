package com.myongjiway.core.api.controller.v1.request

import com.myongjiway.core.api.domain.ExampleData

data class ExampleRequestDto(
    val data: String,
) {
    fun toExampleData(): ExampleData {
        return ExampleData(data, data)
    }
}
