@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.api.controller.v1.token

import com.myongjiway.core.api.auth.security.domain.JwtValidator
import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserRepository
import io.jsonwebtoken.Jwts.*
import io.jsonwebtoken.Jwts.SIG.*
import io.jsonwebtoken.security.Keys.*
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class JwtProviderTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var userRepository: UserRepository
            lateinit var sut: JwtValidator
            val userId = "1234"

            beforeTest {
                jwtProperty = mockk()
                userRepository = mockk()
                sut = JwtValidator(jwtProperty, userRepository)

                every { jwtProperty.accessToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.accessToken.expiration } returns 1000
                every { jwtProperty.refreshToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.refreshToken.expiration } returns 10000
                every { userRepository.findUserById(userId.toLong()) } returns User(
                    id = userId.toLong(),
                    profileImg = "profileImg.img",
                    name = "test",
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

                    // when
                    val authentication = sut.getAuthentication(token)

                    // then
                    authentication shouldNotBe null
                }

                scenario("토큰의 signature가 secret key로 생성이 되지 않았다면 INVALID_TOKEN 을 반환한다.") {
                    // given
                    val invalidSecret = jwtProperty.accessToken.secret + "invalid"
                    val invalidToken =
                        builder().subject(userId).signWith(hmacShaKeyFor(invalidSecret.toByteArray()), HS512)
                            .compact()

                    // when
                    val actual = kotlin.runCatching { sut.getAuthentication(invalidToken) }
                        .exceptionOrNull()

                    // then
                    verify(exactly = 0) {
                        userRepository.findUserById(userId.toLong())
                    }
                    actual?.shouldBeInstanceOf<CoreApiException>()
                    (actual as CoreApiException).errorType shouldBe ErrorType.INVALID_TOKEN_ERROR
                }
            }
        },
    )
