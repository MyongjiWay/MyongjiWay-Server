package com.myongjiway.core.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long,
    val profileImg: String,
    val name: String,
    val password: String,
    val providerId: String,
    val providerType: ProviderType,
    val role: Role,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
