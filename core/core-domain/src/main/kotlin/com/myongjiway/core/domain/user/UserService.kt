package com.myongjiway.core.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userUpdater: com.myongjiway.core.domain.user.UserUpdater,
) {
    fun inactive(providerId: String): Long = userUpdater.inactive(providerId)
}
