@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.token

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.core.auth.security.domain.JwtProvider
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
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
import java.util.Date

class JwtProviderTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var userRepository: UserRepository
            lateinit var sut: JwtProvider
            val userId = "1234"

            beforeTest {
                jwtProperty = mockk()
                userRepository = mockk()
                sut = JwtProvider(jwtProperty, userRepository)

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

            feature("토큰 생성") {
                scenario("AccessToken을 생성한다.") {
                    // given
                    val tokenType = TokenType.ACCESS
                    val now = System.currentTimeMillis()

                    // when
                    val actual = tokenType.generate(
                        Date(now + jwtProperty.accessToken.expiration),
                        sut.generateAccessTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<AccessToken>()
                    actual.userId shouldBe "1234"
                    actual.expiration shouldBe now + jwtProperty.accessToken.expiration
                }

                scenario("RefreshToken을 생성한다.") {
                    // given
                    val tokenType = TokenType.REFRESH
                    val now = System.currentTimeMillis()

                    // when
                    val actual = tokenType.generate(
                        Date(now + jwtProperty.refreshToken.expiration),
                        sut.generateAccessTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<RefreshToken>()
                    actual.userId shouldBe "1234"
                    actual.expiration shouldBe now + jwtProperty.refreshToken.expiration
                }
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
