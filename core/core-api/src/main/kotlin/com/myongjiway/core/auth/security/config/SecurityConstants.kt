package com.myongjiway.core.auth.security.config

object SecurityConstants {
    val EXCLUDED_URLS = listOf(
        "/auth/.*".toRegex(),
        "/actuator/.*".toRegex(),
        "/favicon\\.ico".toRegex(),
        "/error".toRegex(),
        "/".toRegex(),
        "/h2-console/.*".toRegex(),
        "/docs/.*".toRegex(),
    )

    val PERMIT_ALL_URLS = listOf(
        "/auth/**",
        "/actuator/**",
        "/favicon.ico",
        "/error",
        "/",
        "/h2-console/**",
        "/docs/**",
    )

    val ROLE_ADMIN_URLS = mapOf(
        "/admin/**" to "ADMIN",
    )
}
