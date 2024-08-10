package com.myongjiway.token

import com.myongjiway.error.CoreErrorType
import com.myongjiway.error.CoreException
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserReader
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TokenServiceTest :
    FeatureSpec(
        {
            lateinit var tokenReader: TokenReader
            lateinit var tokenAppender: TokenAppender
            lateinit var tokenGenerator: TokenGenerator
            lateinit var userReader: UserReader
            lateinit var tokenProcessor: TokenProcessor
            lateinit var sut: TokenService

            beforeTest {
                tokenReader = mockk()
                tokenAppender = mockk()
                tokenGenerator = mockk()
                userReader = mockk()
                tokenProcessor = mockk()
                sut = TokenService(tokenAppender, tokenGenerator, tokenReader, userReader, tokenProcessor)

                every { tokenGenerator.generateAccessTokenByUserId(any()) } returns AccessToken(
                    "1000",
                    "newAccessToken",
                    1000,
                )

                every { tokenGenerator.generateRefreshTokenByUserId(any()) } returns RefreshToken(
                    "1000",
                    "newRefreshToken",
                    10000,
                )

                every { tokenAppender.upsert(any(), any(), any()) } returns 1
            }

            feature("토큰 갱신") {
                scenario("RefreshToken의 expiration이 지나지 않았다면 AccessToken을 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() + 100000
                    every { tokenReader.findByToken(any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        expiration,
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
                    sut.refresh(RefreshData("refreshToken"))

                    // then
                    verify(exactly = 0) { tokenGenerator.generateRefreshTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    verify(exactly = 0) { tokenAppender.upsert(any(), any(), any()) }
                }

                scenario("RefreshToken의 expiration이 지났다면 RefreshToken과 AccessToken을 같이 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() - 100000

                    every { tokenReader.findByToken(any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        expiration,
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

                    every { tokenAppender.upsert(any(), any(), any()) } returns 1

                    // when
                    sut.refresh(RefreshData("refreshToken"))

                    // then
                    verify(exactly = 1) { tokenGenerator.generateRefreshTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    verify(exactly = 1) { tokenAppender.upsert(any(), any(), any()) }
                }

                scenario("RefreshToken이 없다면 UNAUTHORIZED_TOKEN 에러를 반환한다.") {
                    // given
                    every { tokenReader.findByToken(any()) } returns null

                    // when
                    val actual = runCatching { sut.refresh(RefreshData("refreshToken")) }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.UNAUTHORIZED_TOKEN)
                }

                scenario("User가 없다면 USER_NOT_FOUND 에러를 반환한다.") {
                    // given
                    val expiration = System.currentTimeMillis() - 10000
                    every { tokenReader.findByToken(any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        expiration,
                    )

                    every { userReader.find(1000) } returns null

                    // when
                    val actual = runCatching { sut.refresh(RefreshData("refreshToken")) }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.USER_NOT_FOUND)
                }
            }

            feature("토큰 삭제") {
                scenario("RefreshToken 삭제에 성공한다.") {
                    // given
                    every { tokenReader.findByToken(any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        10000,
                    )
                    every { tokenProcessor.deleteToken(any()) } returns Unit

                    // when
                    sut.delete("refreshToken")

                    // then
                    verify(exactly = 1) { tokenReader.findByToken("refreshToken") }
                    verify(exactly = 1) { tokenProcessor.deleteToken("refreshToken") }
                }

                scenario("RefreshToken이 존재하지 않는다면 NOT_FOUND_TOKEN 에러를 반환한다.") {
                    // given
                    every { tokenReader.findByToken(any()) } returns null

                    // when
                    val actual = runCatching { sut.delete("refreshToken") }

                    // then
                    actual.exceptionOrNull() shouldBe CoreException(CoreErrorType.NOT_FOUND_TOKEN)
                }
            }
        },
    )
