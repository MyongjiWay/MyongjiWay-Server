package com.myongjiway.storage.db.core.user

import com.myongjiway.storage.db.core.common.BaseEntity
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class UserEntity(
    private var profileImg: String,
    private var name: String,
    val providerId: String,
    @Enumerated(value = EnumType.STRING)
    val providerType: ProviderType,
    @Enumerated(value = EnumType.STRING)
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
