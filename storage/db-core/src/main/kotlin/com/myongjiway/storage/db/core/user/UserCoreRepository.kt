package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
import jakarta.transaction.Transactional
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

    @Transactional
    override fun append(
        providerId: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): Long = userJpaRepository.save(
        UserEntity(
            providerId = providerId,
            profileImg = profileImg,
            name = name,
            providerType = providerType,
            role = role,
        ),
    ).id!!

    @Transactional
    override fun modify(providerId: String, profileImg: String, name: String, role: Role): Long {
        val user = userJpaRepository.findByProviderId(providerId)
        user?.update(profileImg, name, role)

        return user?.id!!
    }

    @Transactional
    override fun upsert(
        providerId: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): Long {
        val user = userJpaRepository.findByProviderId(providerId)
            ?: return append(providerId, profileImg, name, providerType, role)

        user.update(profileImg, name, role)
        return user.id!!
    }

    @Transactional
    override fun inactive(providerId: String): Long {
        val user = userJpaRepository.findByProviderId(providerId)
        user?.inactive()
        return user?.id!!
    }
}
