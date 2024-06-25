package com.myongjiway.storage.db.core.user

import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class UserEntity(
    val email: String,
    val profileImg: String,
    val name: String,
    val providerId: String,
    val providerType: String,
) : BaseEntity()
