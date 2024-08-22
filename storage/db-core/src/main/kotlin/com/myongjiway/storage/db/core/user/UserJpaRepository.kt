package com.myongjiway.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByIdAndIsDeleted(id: Long, deleted: Boolean): UserEntity?
    fun findByProviderIdAndIsDeleted(providerId: String, deleted: Boolean): UserEntity?
}
