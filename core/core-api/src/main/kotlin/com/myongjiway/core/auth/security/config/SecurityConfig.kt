package com.myongjiway.core.auth.security.config

import com.myongjiway.core.auth.security.domain.JwtProvider
import com.myongjiway.core.auth.security.jwt.JwtAccessDeniedHandler
import com.myongjiway.core.auth.security.jwt.JwtAuthenticationEntryPoint
import com.myongjiway.core.auth.security.log.RequestResponseLoggingFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
internal class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val requestResponseLoggingFilter: RequestResponseLoggingFilter,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { obj: CorsConfigurer<HttpSecurity> ->
                obj.disable()
            }
            .sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity> ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .headers { header ->
                header.frameOptions { it.sameOrigin() }
            }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests { authorizeRequests ->
                SecurityConstants.PERMIT_ALL_URLS.forEach { url ->
                    authorizeRequests.requestMatchers(url).permitAll()
                }
                SecurityConstants.ROLE_ADMIN_URLS.forEach { (url, role) ->
                    authorizeRequests.requestMatchers(url).hasRole(role)
                }
                authorizeRequests.anyRequest().authenticated()
            }.with(JwtSecurityConfig(jwtProvider, requestResponseLoggingFilter)) {}

        return http.build()
    }
}
