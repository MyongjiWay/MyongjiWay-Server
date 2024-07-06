package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType

class UserEntityProxy(
    override val id: Long,
    profileImg: String,
    name: String,
    providerId: String,
    providerType: ProviderType,
) : UserEntity(
    profileImg = profileImg,
    name = name,
    providerId = providerId,
    providerType = providerType,
)
