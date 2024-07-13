package com.myongjiway.storage.db.core.user

import com.myongjiway.storage.db.core.common.BaseEntity
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
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
    private var role: Role,
) : BaseEntity() {
    fun toUser() = User(
        id = id!!,
        profileImg = profileImg,
        name = name,
        providerId = providerId,
        providerType = providerType,
        role = role,
        createdAt = createdAt!!,
        updatedAt = updatedAt!!,
    )

    fun update(profileImg: String, name: String, role: Role) {
        this.profileImg = profileImg
        this.name = name
        this.role = role
    }
}
