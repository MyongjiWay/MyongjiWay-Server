package com.myongjiway.storage.db.core.user

import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import java.time.LocalDateTime

class UserEntityProxy(
    override val id: Long,
    override val createdAt: LocalDateTime,
    override var updatedAt: LocalDateTime?,
    profileImg: String,
    name: String,
    providerId: String,
    providerType: ProviderType,
    role: Role,
) : UserEntity(
    profileImg = profileImg,
    name = name,
    providerId = providerId,
    providerType = providerType,
    role = role,
)
