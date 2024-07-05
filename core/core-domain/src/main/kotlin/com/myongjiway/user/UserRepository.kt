package com.myongjiway.user

interface UserRepository {
    fun findUserById(id: Long): User?
}
