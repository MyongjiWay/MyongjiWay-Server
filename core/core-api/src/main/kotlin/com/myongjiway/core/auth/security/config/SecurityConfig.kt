package com.myongjiway.core.auth.security.config

import com.myongjiway.core.auth.security.jwt.JwtAccessDeniedHandler
import com.myongjiway.core.auth.security.jwt.JwtAuthenticationEntryPoint
import com.myongjiway.core.auth.security.jwt.JwtProvider
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
internal class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
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
                authorizeRequests
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            }.with(JwtSecurityConfig(jwtProvider)) {}

        return http.build()
    }
}
