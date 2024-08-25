package com.myongjiway.web.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val userDetails = userDetailsService.loadUserByUsername(username)

        if (passwordEncoder.matches(password, userDetails.password)) {
            return UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
        } else {
            throw BadCredentialsException("Invalid password")
        }
    }

    override fun supports(authentication: Class<*>): Boolean = UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}
