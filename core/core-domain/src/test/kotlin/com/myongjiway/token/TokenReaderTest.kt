package com.myongjiway.token

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenReader
import com.myongjiway.core.domain.token.TokenRepository
import com.myongjiway.core.domain.token.TokenType
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
                    val token = "token"
                    every { tokenRepository.find(token) } returns Token(
                        userId = "1000",
                        token = token,
                        expiration = 1721041381000,
                        tokenType = TokenType.REFRESH,
                    )

                    // when
                    val refreshToken = sut.find(token)

                    // then
                    refreshToken?.token shouldBe token
                    refreshToken?.userId shouldBe "1000"
                }

                scenario("토큰이 존재하지 않으면 TOKEN_NOT_FOUND 에러를 반환한다.") {
                    // given
                    every { tokenRepository.find("token") } throws CoreException(CoreErrorType.TOKEN_NOT_FOUND)

                    // when
                    val actual = kotlin.runCatching { sut.find("token") }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.TOKEN_NOT_FOUND)
                }
            }
        },
    )
