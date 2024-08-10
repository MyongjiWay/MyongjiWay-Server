package com.myongjiway.token

interface TokenRepository {
    fun upsert(userId: Long, token: String, expiration: Long): Long
    fun find(userId: Long, refreshToken: String): Token?
}
