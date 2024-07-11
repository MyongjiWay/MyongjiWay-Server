package com.myongjiway.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByProviderId(providerId: String): UserEntity?
}
