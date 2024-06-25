package com.myongjiway.storage.db.core.token

import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "token")
class TokenEntity(
    val userId: String,
    val token: String,
    val expiration: Long,
) : BaseEntity()
