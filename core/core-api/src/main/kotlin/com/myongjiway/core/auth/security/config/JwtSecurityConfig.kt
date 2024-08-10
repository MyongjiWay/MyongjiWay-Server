package com.myongjiway.core.auth.security.config

import com.myongjiway.core.auth.security.domain.JwtValidator
import com.myongjiway.core.auth.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class JwtSecurityConfig(
    private val jwtValidator: JwtValidator,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtAuthenticationFilter(jwtValidator)
        http
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
