package com.myongjiway.token

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TokenReaderTest :
    FeatureSpec(
        {
            lateinit var tokenRepository: TokenRepository
            lateinit var sut: TokenReader

            beforeTest {
                tokenRepository = mockk()
                sut = TokenReader(tokenRepository)
            }

            feature("토큰 조회") {
                scenario("토큰이 존재하면 조회에 성공한다.") {
                    // given
                    val userId = 1000L
                    val token = "token"
                    every { tokenRepository.find(userId, token) } returns RefreshToken(
                        userId = userId.toString(),
                        token = token,
                        expiration = 1721041381000,
                    )

                    // when
                    val refreshToken = sut.findByTokenAndUserId(userId, token)

                    // then
                    refreshToken?.token shouldBe token
                    refreshToken?.userId shouldBe userId.toString()
                }

                scenario("토큰이 존재하지 않으면 null을 반환한다.") {
                    // given
                    every { tokenRepository.find(1000L, "token") } returns null

                    // when
                    val refreshToken = sut.findByTokenAndUserId(1000L, "token")

                    // then
                    refreshToken shouldBe null
                }
            }
        },
    )
