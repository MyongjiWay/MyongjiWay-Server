package com.myongjiway.storage.db.core.token

import com.myongjiway.token.TokenRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class TokenCoreRepository(
    private val tokenJpaRepository: TokenJpaRepository,
) : TokenRepository {

    @Transactional
    override fun upsert(userId: Long, token: String, expiration: Long): Long {
        val tokenEntity = tokenJpaRepository.findByUserId(userId)
            ?: return tokenJpaRepository.save(TokenEntity(userId, token, expiration)).id!!
        tokenEntity.update(token, expiration)
        return tokenEntity.id!!
    }
}
