package com.myongjiway.storage.db.core.user

import com.myongjiway.storage.db.core.common.BaseEntity
import com.myongjiway.user.ProviderType
import com.myongjiway.user.User
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class UserEntity(
    private var profileImg: String,
    private var name: String,
    val providerId: String,
    val providerType: ProviderType,
) : BaseEntity() {
    fun toUser() = User(
        id = id!!,
        profileImg = profileImg,
        name = name,
        providerId = providerId,
        providerType = providerType,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
    )
}
