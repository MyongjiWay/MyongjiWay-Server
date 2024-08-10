package com.myongjiway.token

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TokenServiceTest :
    FeatureSpec(
        {
            lateinit var tokenReader: TokenReader
            lateinit var tokenAppender: TokenAppender
            lateinit var tokenGenerator: TokenGenerator
            lateinit var sut: TokenService

            beforeTest {
                tokenReader = mockk()
                tokenAppender = mockk()
                tokenGenerator = mockk()
                sut = TokenService(tokenAppender, tokenGenerator, tokenReader)
            }

            feature("토큰 갱신") {
                scenario("RefreshToken의 expiration이 지나지 않았다면 AccessToken을 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() + 10000
                    every { tokenReader.findByTokenAndUserId(any(), any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        expiration,
                    )

                    every { tokenGenerator.generateAccessTokenByUserId(any()) } returns AccessToken(
                        "1000",
                        "accessToken",
                        1000,
                    )

                    // when
                    sut.refresh(RefreshData(1000, "refreshToken"))

                    // then
                    verify(exactly = 0) { tokenGenerator.generateRefreshTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    verify(exactly = 0) { tokenAppender.upsert(any(), any(), any()) }
                }

                scenario("RefreshToken의 expiration이 지났다면 RefreshToken과 AccessToken을 같이 갱신한다.") {
                    // given
                    val expiration = System.currentTimeMillis() - 10000
                    every { tokenReader.findByTokenAndUserId(any(), any()) } returns RefreshToken(
                        "1000",
                        "refreshToken",
                        expiration,
                    )
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

                    // when
                    sut.refresh(RefreshData(1000, "refreshToken"))

                    // then
                    verify(exactly = 1) { tokenGenerator.generateRefreshTokenByUserId(any()) }
                    verify(exactly = 1) { tokenGenerator.generateAccessTokenByUserId(any()) }
                    verify(exactly = 1) { tokenAppender.upsert(any(), any(), any()) }
                }
            }
        },
    )
