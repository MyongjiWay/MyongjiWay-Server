@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.domain.auth

import com.myongjiway.core.domain.token.Token
import com.myongjiway.core.domain.token.TokenAppender
import com.myongjiway.core.domain.token.TokenGenerator
import com.myongjiway.core.domain.token.TokenType
import com.myongjiway.core.domain.user.UserAppender
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class AuthServiceTest :
    FeatureSpec(
        {
            lateinit var userAppender: UserAppender
            lateinit var tokenAppender: TokenAppender
            lateinit var tokenGenerator: TokenGenerator
            lateinit var sut: AuthService

            val kakaoLoginData = KakaoLoginData(
                providerId = "1234",
                username = "test",
                profileImg = "test",
            )

            beforeTest {
                tokenAppender = mockk()
                userAppender = mockk()
                tokenGenerator = mockk()

                sut = AuthService(userAppender, tokenAppender, tokenGenerator)
            }

            feature("카카오 로그인") {
                scenario("입력 받은 providerId가 DB에 존재하지 않으면 유저를 생성하고 토큰을 반환한다.") {
                    // given
                    every { userAppender.upsert(any(), any(), any(), any(), any()) } returns 1000L
                    every { tokenAppender.upsert(any(), any(), any()) } returns 1000L
                    every { tokenGenerator.generateAccessTokenByUserId(any()) } returns Token("1000", "accessToken", 1000, TokenType.ACCESS)
                    every { tokenGenerator.generateRefreshTokenByUserId(any()) } returns Token("1000", "refreshToken", 10000, TokenType.REFRESH)

                    // when
                    val actual = sut.kakaoLogin(kakaoLoginData)

                    // then
                    actual.accessToken shouldNotBe null
                    actual.refreshToken shouldNotBe null
                }
            }
        },
    )
