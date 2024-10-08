package com.myongjiway.core.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userUpdater: UserUpdater,
) {
    fun inactive(providerId: String): Long = userUpdater.inactive(providerId)
}
