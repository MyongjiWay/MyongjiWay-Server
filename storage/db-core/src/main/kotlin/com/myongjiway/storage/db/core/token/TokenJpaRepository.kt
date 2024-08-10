package com.myongjiway.storage.db.core.token

import org.springframework.data.jpa.repository.JpaRepository

interface TokenJpaRepository : JpaRepository<TokenEntity, Long> {
    fun findByUserId(userId: Long): TokenEntity?
    fun findByToken(token: String): TokenEntity?
}
