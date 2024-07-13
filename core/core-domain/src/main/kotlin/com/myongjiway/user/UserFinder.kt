package com.myongjiway.user

import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {
    fun find(id: Long): User? = userRepository.findUserById(id)
    fun find(providerId: String): User? = userRepository.findUserByProviderId(providerId)
}
