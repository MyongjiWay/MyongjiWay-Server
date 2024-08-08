package com.myongjiway.core.auth.security.config

import com.myongjiway.core.auth.security.domain.JwtProvider
import com.myongjiway.core.auth.security.jwt.JwtFilter
import com.myongjiway.core.auth.security.log.RequestResponseLoggingFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class JwtSecurityConfig(
    private val jwtProvider: JwtProvider,
    private val requestResponseLoggingFilter: RequestResponseLoggingFilter,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(jwtProvider)
        http
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(requestResponseLoggingFilter, JwtFilter::class.java)
    }
}
