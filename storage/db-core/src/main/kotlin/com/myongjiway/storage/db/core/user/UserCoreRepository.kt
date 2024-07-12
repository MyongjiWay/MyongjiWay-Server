package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findUserById(id: Long): User? {
        val user = userJpaRepository.findById(id).orElseThrow()
        return user.toUser()
    }

    override fun findUserByProviderId(providerId: String): User? {
        val user = userJpaRepository.findByProviderId(providerId)
        return user?.toUser()
    }

    override fun append(
        providerId: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): User {
        val savedUser = userJpaRepository.save(
            UserEntity(
                providerId = providerId,
                profileImg = profileImg,
                name = name,
                providerType = providerType,
                role = role,
            ),
        )
        return savedUser.toUser()
    }
}
