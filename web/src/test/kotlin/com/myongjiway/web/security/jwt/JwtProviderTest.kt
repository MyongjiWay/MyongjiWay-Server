@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.web.security.jwt

import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.token.TokenValidator
import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserReader
import io.jsonwebtoken.Jwts.*
import io.jsonwebtoken.Jwts.SIG.*
import io.jsonwebtoken.security.Keys.*
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.mock.web.MockHttpServletRequest
import java.time.LocalDateTime

class JwtProviderTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var tokenValidator: TokenValidator
            lateinit var userReader: UserReader
            lateinit var sut: JwtProvider
            val userId = "1234"

            beforeTest {
                jwtProperty = mockk()
                tokenValidator = mockk()
                userReader = mockk()
                sut = JwtProvider(jwtProperty, tokenValidator, userReader)

                every { jwtProperty.accessToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.accessToken.expiration } returns 1000
                every { jwtProperty.refreshToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.refreshToken.expiration } returns 10000
                every { userReader.find(userId.toLong()) } returns User(
                    id = userId.toLong(),
                    profileImg = "profileImg.img",
                    name = "test",
                    password = "",
                    providerId = "providerId",
                    providerType = ProviderType.KAKAO,
                    role = Role.USER,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            }

            feature("토큰 검증") {
                scenario("토큰의 signature가 secret key로 생성이 되었다면 true를 반환한다.") {
                    // given
                    val token = builder().subject(userId)
                        .signWith(hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()), HS512)
                        .compact()
                    val mockHttpServletRequest = MockHttpServletRequest()

                    // when
                    val authentication = sut.getAuthentication(mockHttpServletRequest, token)

                    // then
                    authentication shouldNotBe null
                }
            }
        },
    )
