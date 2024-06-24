package com.myongjiway.core.auth.security.jwt

import com.myongjiway.core.auth.security.token.service.JwtService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class JwtSecurityConfig(
    private val jwtService: JwtService,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(jwtService)
        http
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
