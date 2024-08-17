package com.myongjiway.storage.db.core.token

import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenType
import com.myongjiway.storage.db.core.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "refresh_token")
class TokenEntity(
    val userId: Long,
    var token: String,
    private var expiration: Long,
    @Enumerated(EnumType.STRING)
    val tokenType: TokenType,
) : BaseEntity() {
    fun toRefreshToken(): Token = Token(userId.toString(), token, expiration, tokenType)

    fun update(token: String, expiration: Long) {
        this.token = token
        this.expiration = expiration
    }
}
