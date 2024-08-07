package com.myongjiway.common.security

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class MockSecurityFilter : Filter {
    override fun init(filterConfig: FilterConfig) {}
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse?,
        chain: FilterChain,
    ) {
        val user = User(
            id = 1000L,
            profileImg = "test.img",
            name = "test",
            providerId = "1234",
            providerType = ProviderType.KAKAO,
            role = Role.USER,
            createdAt = null,
            updatedAt = null,
        )
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user, null, listOf(GrantedAuthority { user?.role?.value }))

        chain.doFilter(request, response)
    }

    override fun destroy() {
        SecurityContextHolder.clearContext()
    }

    fun getFilters(mockHttpServletRequest: MockHttpServletRequest) {}
}
