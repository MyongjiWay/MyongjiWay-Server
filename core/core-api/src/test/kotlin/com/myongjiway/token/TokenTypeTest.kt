package com.myongjiway.token

import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.core.auth.security.domain.JwtProvider
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import java.util.Date

class TokenTypeTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var jwtProvider: JwtProvider

            beforeTest {
                jwtProvider = mockk()
                jwtProperty = mockk()
                every { jwtProperty.accessToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.accessToken.expiration } returns 1000
                every { jwtProperty.refreshToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.refreshToken.expiration } returns 10000
            }

            feature("토큰 타입에 따른 토큰 생성") {
                val userId = "1234"
                val now = System.currentTimeMillis()

                scenario("토큰 타입이 ACCESS_TOKEN일 경우 AccessToken을 생성한다.") {
                    // given
                    every { jwtProvider.generateAccessTokenByUserId(userId) } returns AccessToken(
                        userId,
                        "token",
                        now,
                    )

                    // when
                    val actual = TokenType.ACCESS.generate(
                        Date(now + jwtProperty.accessToken.expiration),
                        jwtProvider.generateAccessTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<AccessToken>()
                    actual.userId shouldBe userId
                    actual.expiration shouldBe now + jwtProperty.accessToken.expiration
                }

                scenario("토큰 타입이 REFRESH_TOKEN일 경우 RefreshToken을 생성한다.") {
                    // given
                    every { jwtProvider.generateRefreshTokenByUserId(userId) } returns RefreshToken(
                        userId,
                        "token",
                        now,
                    )

                    // when
                    val actual = TokenType.REFRESH.generate(
                        Date(now + jwtProperty.refreshToken.expiration),
                        jwtProvider.generateRefreshTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<RefreshToken>()
                    actual.userId shouldBe userId
                    actual.expiration shouldBe now + jwtProperty.refreshToken.expiration
                }
            }
        },
    )
