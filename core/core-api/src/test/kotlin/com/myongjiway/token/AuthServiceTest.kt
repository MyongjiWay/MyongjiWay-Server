@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.token

import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.core.auth.security.domain.AuthService
import com.myongjiway.core.auth.security.domain.JwtProvider
import com.myongjiway.core.auth.security.domain.KakaoLoginData
import com.myongjiway.user.UserAppender
import com.myongjiway.user.UserRepository
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class AuthServiceTest :
    FeatureSpec(
        {
            lateinit var userRepository: UserRepository
            lateinit var sut: AuthService
            lateinit var jwtProvider: JwtProvider
            lateinit var jwtProperty: JwtProperty
            lateinit var userAppender: UserAppender
            lateinit var tokenAppender: TokenAppender

            val kakaoLoginData = KakaoLoginData(
                providerId = "1234",
                username = "test",
                profileImg = "test",
            )

            beforeTest {
                jwtProperty = mockk()
                userRepository = mockk()
                tokenAppender = mockk()

                jwtProvider = JwtProvider(jwtProperty, userRepository)
                userAppender = UserAppender(userRepository)
                sut = AuthService(jwtProvider, userAppender, tokenAppender)

                every { jwtProperty.accessToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.accessToken.expiration } returns 1000
                every { jwtProperty.refreshToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.refreshToken.expiration } returns 10000
            }

            feature("카카오 로그인") {
                scenario("입력 받은 providerId가 DB에 존재하지 않으면 유저를 생성하고 토큰을 반환한다.") {
                    // given
                    every { userAppender.upsert(any(), any(), any(), any(), any()) } returns 1000L
                    every { tokenAppender.upsert(any(), any(), any()) } returns 1000L

                    // when
                    val actual = sut.kakaoLogin(kakaoLoginData)

                    // then
                    actual.accessToken shouldNotBe null
                    actual.refreshToken shouldNotBe null
                }
            }
        },
    )
