package com.myongjiway.user

interface UserRepository {
    fun findUserById(id: Long): User?
    fun findUserByProviderId(providerId: String): User?
}
