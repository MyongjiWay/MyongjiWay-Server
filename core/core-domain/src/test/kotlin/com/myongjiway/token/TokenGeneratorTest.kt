package com.myongjiway.token

import com.myongjiway.core.domain.token.JwtProperty
import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenAppender
import com.myongjiway.core.domain.token.TokenGenerator
import com.myongjiway.core.domain.token.TokenType
import com.myongjiway.core.domain.token.TokenValidator
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import java.util.Date

class TokenGeneratorTest :
    FeatureSpec(
        {
            lateinit var jwtProperty: JwtProperty
            lateinit var tokenValidator: TokenValidator
            lateinit var tokenAppender: TokenAppender
            lateinit var sut: TokenGenerator
            val userId = "1234"

            beforeTest {
                jwtProperty = JwtProperty().apply {
                    accessToken = JwtProperty.TokenProperties().apply {
                        expiration = 10000
                        secret =
                            "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                    }
                    refreshToken = JwtProperty.TokenProperties().apply {
                        expiration = 1000000
                        secret =
                            "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                    }
                }
                tokenAppender = mockk()
                tokenValidator = mockk()
                sut = TokenGenerator(jwtProperty, tokenValidator, tokenAppender)
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
                    actual.shouldBeInstanceOf<Token>()
                    actual.tokenType shouldBe TokenType.ACCESS
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
                    actual.shouldBeInstanceOf<Token>()
                    actual.tokenType shouldBe TokenType.REFRESH
                    actual.userId shouldBe "1234"
                    actual.expiration shouldBe now + jwtProperty.refreshToken.expiration
                }
            }
        },
    )
