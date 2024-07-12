package com.myongjiway.user

interface UserRepository {
    fun findUserById(id: Long): User?
    fun findUserByProviderId(providerId: String): User?
    fun append(providerId: String, profileImg: String, name: String, providerType: ProviderType, role: Role): User
}
