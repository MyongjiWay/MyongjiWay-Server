package com.myongjiway.token

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.util.Date

class TokenGeneratorTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var sut: TokenGenerator
            val userId = "1234"

            beforeTest {
                jwtProperty = JwtProperty().apply {
                    accessToken = JwtProperty.TokenProperties().apply {
                        expiration = 10000
                        secret = "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                    }
                    refreshToken = JwtProperty.TokenProperties().apply {
                        expiration = 1000000
                        secret = "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                    }
                }
                sut = TokenGenerator(jwtProperty)
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
        },
    )