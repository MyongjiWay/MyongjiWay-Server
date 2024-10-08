package com.myongjiway.web.security.config

import com.myongjiway.web.security.jwt.JwtAuthenticationFilter
import com.myongjiway.web.security.jwt.JwtProvider
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class JwtSecurityConfig(
    private val jwtProvider: JwtProvider,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtAuthenticationFilter(jwtProvider)
        http
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
