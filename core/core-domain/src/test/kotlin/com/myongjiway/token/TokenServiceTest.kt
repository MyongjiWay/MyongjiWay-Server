package com.myongjiway.token

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import com.myongjiway.core.domain.token.RefreshData
import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenGenerator
import com.myongjiway.core.domain.token.TokenProcessor
import com.myongjiway.core.domain.token.TokenReader
import com.myongjiway.core.domain.token.TokenService
import com.myongjiway.core.domain.token.TokenType
import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import com.myongjiway.core.domain.user.UserReader
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TokenServiceTest :
    FeatureSpec(
        {
            lateinit var tokenReader: TokenReader
            lateinit var tokenGenerator: TokenGenerator
            lateinit var userReader: UserReader
            lateinit var tokenProcessor: TokenProcessor
            lateinit var sut: TokenService

            beforeTest {
                tokenReader = mockk()
                tokenGenerator = mockk()
                userReader = mockk()
                tokenProcessor = mockk()
                sut = TokenService(tokenGenerator, tokenReader, userReader, tokenProcessor)

                every { tokenGenerator.generateAccessTokenByUserId(any()) } returns Token(
                    "1000",
                    "newAccessToken",
                    1000,
                    TokenType.ACCESS,
                )
            }

            feature("토큰 갱신") {
                scenario("RefreshToken의 expiration이 지나지 않았다면 AccessToken을 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() + 100000
                    every { tokenReader.find(any()) } returns Token(
                        "1000",
                        "refreshToken",
                        expiration,
                        TokenType.REFRESH,
                    )

                    every { userReader.find(1000L) } returns User(
                        1000,
                        "email",
                        "password",
                        "name",
                        ProviderType.KAKAO,
                        Role.USER,
                        mockk(),
                        mockk(),
                    )

                    every { tokenGenerator.refresh(any()) } returns Token(
                        "1000",
                        "refreshToken",
                        expiration,
                        TokenType.REFRESH,
                    )

                    // when
                    sut.refresh(RefreshData("refreshToken"))

                    // then
                    verify(exactly = 0) { tokenGenerator.generateRefreshTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                }

                scenario("RefreshToken의 expiration이 지났다면 RefreshToken과 AccessToken을 같이 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() - 100000
                    every { tokenGenerator.refresh(any()) } returns Token(
                        "1000",
                        "newRefreshToken",
                        10000,
                        TokenType.REFRESH,
                    )

                    every { tokenReader.find(any()) } returns Token(
                        "1000",
                        "refreshToken",
                        expiration,
                        TokenType.REFRESH,
                    )

                    every { userReader.find(1000L) } returns User(
                        1000,
                        "email",
                        "password",
                        "name",
                        ProviderType.KAKAO,
                        Role.USER,
                        mockk(),
                        mockk(),
                    )

                    // when
                    val actual = sut.refresh(RefreshData("refreshToken"))

                    // then
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    actual.refreshToken shouldBe "newRefreshToken"
                }

                scenario("RefreshToken이 없다면 UNAUTHORIZED_TOKEN 에러를 반환한다.") {
                    // given
                    every { tokenReader.find(any()) } throws CoreException(CoreErrorType.TOKEN_NOT_FOUND)

                    // when
                    val actual = runCatching { sut.refresh(RefreshData("refreshToken")) }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.TOKEN_NOT_FOUND)
                }

                scenario("User가 없다면 USER_NOT_FOUND 에러를 반환한다.") {
                    // given
                    val expiration = System.currentTimeMillis() - 10000
                    every { tokenReader.find(any()) } returns Token(
                        "1000",
                        "refreshToken",
                        expiration,
                        TokenType.REFRESH,
                    )

                    every { userReader.find(1000) } throws CoreException(CoreErrorType.USER_NOT_FOUND)

                    // when
                    val actual = runCatching { sut.refresh(RefreshData("refreshToken")) }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.USER_NOT_FOUND)
                }
            }

            feature("토큰 삭제") {
                scenario("RefreshToken 삭제에 성공한다.") {
                    // given
                    every { tokenReader.find(any()) } returns Token(
                        "1000",
                        "refreshToken",
                        10000,
                        TokenType.REFRESH,
                    )
                    every { tokenProcessor.deleteToken(any()) } returns Unit

                    // when
                    sut.delete("refreshToken")

                    // then
                    verify(exactly = 1) { tokenReader.find("refreshToken") }
                    verify(exactly = 1) { tokenProcessor.deleteToken("refreshToken") }
                }

                scenario("RefreshToken이 존재하지 않는다면 NOT_FOUND_TOKEN 에러를 반환한다.") {
                    // given
                    every { tokenReader.find(any()) } throws CoreException(CoreErrorType.TOKEN_NOT_FOUND)

                    // when
                    val actual = runCatching { sut.delete("refreshToken") }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.TOKEN_NOT_FOUND)
                }
            }
        },
    )
