package com.myongjiway.storage.db.core.user

import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findUserById(id: Long): User? {
        val userEntity = userJpaRepository.findById(id).orElse(null) ?: return null
        return userEntity.toUser()
    }
}
