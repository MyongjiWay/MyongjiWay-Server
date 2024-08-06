package com.myongjiway.token

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TokenAppenderTest :
    FeatureSpec(
        {
            lateinit var sut: TokenAppender
            lateinit var tokenRepository: TokenRepository

            beforeTest {
                tokenRepository = mockk()
                sut = TokenAppender(tokenRepository)
            }

            feature("토큰이 존재하지 않으면 새로 생성하고 존재하면 업데이트한다.") {
                scenario("토큰이 존재하지 않으면 새로 생성하고 저장에 성공한다.") {
                    // given
                    every { tokenRepository.upsert(123, "token", 1721041381000) } returns 1000L

                    // when
                    val actual = sut.upsert(123, "token", 1721041381000)

                    // then
                    actual shouldBe 1000L
                }

                scenario("토큰이 존재하면 업데이트에 성공한다.") {
                    // given
                    every { tokenRepository.upsert(123, "new token", 1721041381000) } returns 1000L

                    // when
                    val actual = sut.upsert(123, "new token", 1721041381000)

                    // then
                    actual shouldBe 1000L
                }
            }
        },
    )
