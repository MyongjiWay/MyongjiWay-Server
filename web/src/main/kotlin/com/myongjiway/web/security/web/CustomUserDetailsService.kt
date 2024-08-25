package com.myongjiway.web.security.web

import com.myongjiway.core.domain.user.UserReader
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userReader: UserReader,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userReader.findByUsername(username)

        return UserPrincipal(user)
    }
}
