package com.myongjiway.core.domain.user

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    fun find(id: Long): User = userRepository.findUserById(id) ?: throw CoreException(CoreErrorType.USER_NOT_FOUND)
    fun find(providerId: String): User = userRepository.findUserByProviderId(providerId) ?: throw CoreException(CoreErrorType.USER_NOT_FOUND)
}
