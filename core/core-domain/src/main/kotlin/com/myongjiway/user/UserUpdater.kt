package com.myongjiway.user

import org.springframework.stereotype.Component

@Component
class UserUpdater(
    private val userRepository: UserRepository,
) {
    fun modify(providerId: String, profileImg: String, name: String, role: Role): Long =
        userRepository.modify(providerId, profileImg, name, role)

    fun inactive(userId: Long): Long = userRepository.inactive(userId)
}
