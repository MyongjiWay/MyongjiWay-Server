package com.myongjiway.web.security.config

import com.myongjiway.web.security.jwt.JwtAccessDeniedHandler
import com.myongjiway.web.security.jwt.JwtAuthenticationEntryPoint
import com.myongjiway.web.security.jwt.JwtAuthenticationFilter
import com.myongjiway.web.security.web.CustomAuthenticationFailureHandler
import com.myongjiway.web.security.web.CustomAuthenticationProvider
import com.myongjiway.web.security.web.CustomAuthenticationSuccessHandler
import com.myongjiway.web.security.web.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
internal class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val userDetailsService: CustomUserDetailsService,
    private val authenticationSuccessHandler: CustomAuthenticationSuccessHandler,
    private val authenticationFailureHandler: CustomAuthenticationFailureHandler,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = CustomAuthenticationProvider(userDetailsService, passwordEncoder())
        return authenticationProvider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .httpBasic { basic -> basic.disable() }
        .csrf { csrf -> csrf.disable() }
        .authenticationProvider(authenticationProvider())
        .formLogin { form ->
            form.loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
        }
        .headers { header -> header.frameOptions { frameOptions -> frameOptions.disable() } }
        .cors { cors -> cors.disable() }
        .sessionManagement { setSessionManagement() }
        .authorizeHttpRequests(setAuthorizePath())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .exceptionHandling { exceptionHandling ->
            exceptionHandling
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }
        .build()

    private fun setSessionManagement(): Customizer<SessionManagementConfigurer<HttpSecurity>> =
        Customizer { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

    private fun setAuthorizePath(): Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> =
        Customizer {
            it
                .requestMatchers(
                    AntPathRequestMatcher("/auth/**"),
                    AntPathRequestMatcher("/actuator/**"),
                    AntPathRequestMatcher("/favicon.ico"),
                    AntPathRequestMatcher("/error"),
                    AntPathRequestMatcher("/"),
                    AntPathRequestMatcher("/h2-console/**"),
                    AntPathRequestMatcher("/docs/**"),
                    AntPathRequestMatcher("/actuator/prometheus"),
                    AntPathRequestMatcher("/css/**"),
                    AntPathRequestMatcher("/login/**"),
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
        }
}
