package com.myongjiway.core.domain.token

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
            lateinit var tokenGenerator: TokenGenerator

            beforeTest {
                jwtProperty = mockk()
                tokenGenerator = mockk()
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
                    every { tokenGenerator.generateAccessTokenByUserId(userId) } returns Token(
                        userId,
                        "token",
                        now,
                        TokenType.ACCESS,
                    )

                    // when
                    val actual = TokenType.ACCESS.generate(
                        Date(now + jwtProperty.accessToken.expiration),
                        tokenGenerator.generateAccessTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<Token>()
                    actual.tokenType shouldBe TokenType.ACCESS
                    actual.userId shouldBe userId
                    actual.expiration shouldBe now + jwtProperty.accessToken.expiration
                }

                scenario("토큰 타입이 REFRESH_TOKEN일 경우 RefreshToken을 생성한다.") {
                    // given
                    every { tokenGenerator.generateRefreshTokenByUserId(userId) } returns Token(
                        userId,
                        "token",
                        now,
                        TokenType.REFRESH,
                    )

                    // when
                    val actual = TokenType.REFRESH.generate(
                        Date(now + jwtProperty.refreshToken.expiration),
                        tokenGenerator.generateRefreshTokenByUserId(userId).token,
                        userId,
                    )

                    // then
                    actual.shouldBeInstanceOf<Token>()
                    actual.tokenType shouldBe TokenType.REFRESH
                    actual.userId shouldBe userId
                    actual.expiration shouldBe now + jwtProperty.refreshToken.expiration
                }
            }
        },
    )
