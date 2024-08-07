package com.myongjiway.storage.db.core.token

import com.myongjiway.storage.db.core.common.BaseEntity
import com.myongjiway.token.RefreshToken
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "refresh_token")
class TokenEntity(
    val userId: Long,
    private var token: String,
    private var expiration: Long,
) : BaseEntity() {
    fun toRefreshToken(): RefreshToken = RefreshToken(userId.toString(), token, expiration)

    fun update(token: String, expiration: Long) {
        this.token = token
        this.expiration = expiration
    }
}
