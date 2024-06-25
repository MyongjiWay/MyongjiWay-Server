package com.myongjiway.storage.db.core.token

import com.myongjiway.storage.db.core.BaseEntity
import jakarta.persistence.Entity

@Entity
class TokenEntity(
    val userId: String,
    val token: String,
    val expiration: Long,
) : BaseEntity()
