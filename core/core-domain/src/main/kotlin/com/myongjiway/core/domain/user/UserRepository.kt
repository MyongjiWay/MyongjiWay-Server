package com.myongjiway.core.domain.user

interface UserRepository {
    fun findUserById(id: Long): User?
    fun findUserByProviderId(providerId: String): User?
    fun findUserByUsername(username: String): User?
    fun append(providerId: String, profileImg: String, name: String, providerType: ProviderType, role: Role): Long
    fun modify(providerId: String, profileImg: String, name: String, role: Role): Long
    fun upsert(providerId: String, profileImg: String, name: String, providerType: ProviderType, role: Role): Long
    fun inactive(providerId: String): Long
}
