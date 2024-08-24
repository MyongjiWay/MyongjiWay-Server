package com.myongjiway.storage.db.core.user

import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findUserById(id: Long): User? {
        val user = userJpaRepository.findByIdAndIsDeleted(id, false)
        return user?.toUser()
    }

    override fun findUserByProviderId(providerId: String): User? {
        val user = userJpaRepository.findByProviderIdAndIsDeleted(providerId, false)
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
        val user = userJpaRepository.findByProviderIdAndIsDeleted(providerId, false)
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
        val user = userJpaRepository.findByProviderIdAndIsDeleted(providerId, false)
            ?: return append(providerId, profileImg, name, providerType, role)

        user.update(profileImg, name, role)
        return user.id!!
    }

    @Transactional
    override fun inactive(providerId: String): Long {
        val user = userJpaRepository.findByProviderIdAndIsDeleted(providerId, false)
        user?.inactive()
        return user?.id!!
    }
}
