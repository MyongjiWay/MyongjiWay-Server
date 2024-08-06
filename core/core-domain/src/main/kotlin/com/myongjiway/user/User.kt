package com.myongjiway.user

import java.time.LocalDateTime

data class User(
    val id: Long?,
    val profileImg: String,
    val name: String,
    val providerId: String,
    val providerType: ProviderType,
    val role: Role,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun fixture(
            id: Long? = null,
            profileImg: String = "profileImg",
            name: String = "name",
            providerId: String = "providerId",
            providerType: ProviderType = ProviderType.KAKAO,
            role: Role = Role.USER,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
        ): User = User(
            id = id,
            profileImg = profileImg,
            name = name,
            providerId = providerId,
            providerType = providerType,
            role = role,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
