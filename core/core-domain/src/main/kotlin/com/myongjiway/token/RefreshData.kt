package com.myongjiway.token

data class RefreshData(
    val userId: Long,
    val refreshToken: String,
)
