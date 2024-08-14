package com.myongjiway.token

import com.myongjiway.core.domain.token.TokenProcessor
import com.myongjiway.core.domain.token.TokenRepository
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TokenProcessorTest :
    FeatureSpec(
        {
            lateinit var tokenRepository: TokenRepository
            lateinit var sut: TokenProcessor

            beforeTest {
                tokenRepository = mockk()
                sut = TokenProcessor(tokenRepository)
            }

            feature("토큰 삭제") {
                scenario("토큰 삭제에 성공한다.") {
                    // given
                    every { tokenRepository.delete("token") } returns Unit

                    // when
                    val actual = sut.deleteToken("token")

                    // then
                    actual shouldBe Unit
                }
            }
        },
    )
