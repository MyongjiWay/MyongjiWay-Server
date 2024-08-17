package com.myongjiway.storage.db.core.token

import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenRepository
import com.myongjiway.core.domain.token.TokenType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class TokenCoreRepository(
    private val tokenJpaRepository: TokenJpaRepository,
) : TokenRepository {

    @Transactional
    override fun upsert(userId: Long, token: String, expiration: Long): Long {
        val tokenEntity = tokenJpaRepository.findByUserId(userId)
            ?: return tokenJpaRepository.save(TokenEntity(userId, token, expiration, TokenType.REFRESH)).id!!
        tokenEntity.update(token, expiration)
        return tokenEntity.id!!
    }

    override fun find(refreshToken: String): Token? {
        val tokenEntity = tokenJpaRepository.findByToken(refreshToken)
        return tokenEntity?.toRefreshToken()
    }

    @Transactional
    override fun delete(refreshToken: String) {
        tokenJpaRepository.deleteByToken(refreshToken)
    }
}
