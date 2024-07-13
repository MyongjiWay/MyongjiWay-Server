package com.myongjiway.user

interface UserRepository {
    fun findUserById(id: Long): User?
    fun findUserByProviderId(providerId: String): User?
    fun append(providerId: String, profileImg: String, name: String, providerType: ProviderType, role: Role): Long
    fun modify(providerId: String, profileImg: String, name: String, role: Role): Long
    fun upsert(providerId: String, profileImg: String, name: String, providerType: ProviderType, role: Role): Long
}
