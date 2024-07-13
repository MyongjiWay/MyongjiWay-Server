package com.myongjiway.user

import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository,
) {
    fun append(
        providerId: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): Long = userRepository.append(
        providerId = providerId,
        profileImg = profileImg,
        name = name,
        providerType = providerType,
        role = role,
    )
}
