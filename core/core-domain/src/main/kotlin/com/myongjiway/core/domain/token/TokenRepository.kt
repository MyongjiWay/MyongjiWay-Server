package com.myongjiway.core.domain.token

interface TokenRepository {
    fun upsert(userId: Long, token: String, expiration: Long): Long
    fun find(refreshToken: String): Token?
    fun delete(refreshToken: String)
}
