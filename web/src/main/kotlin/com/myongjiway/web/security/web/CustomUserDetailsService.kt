package com.myongjiway.web.security.web

import com.myongjiway.core.domain.user.UserReader
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userReader: UserReader,
) : UserDetailsService {
    override fun loadUserByUsername(providerId: String): UserDetails {
        val user = userReader.find(providerId)

        return UserPrincipal(user)
    }
}
