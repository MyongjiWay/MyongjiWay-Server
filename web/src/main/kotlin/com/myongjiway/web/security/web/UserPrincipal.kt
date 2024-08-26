package com.myongjiway.web.security.web

import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val user: User,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.role.name))

    override fun getPassword(): String = user.providerId

    override fun getUsername(): String = user.name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getId(): Long = user.id

    fun getProfileImg(): String = user.profileImg

    fun getName(): String = user.name

    fun getProviderType(): ProviderType = user.providerType
}
