package com.myongjiway.user

import java.time.LocalDateTime

data class User(
    val id: Long?,
    val profileImg: String,
    val name: String,
    val providerId: String,
    val providerType: ProviderType,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
