package com.myongjiway.token

import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.core.auth.security.jwt.AuthService
import com.myongjiway.core.auth.security.jwt.JwtProvider
import com.myongjiway.core.auth.security.jwt.KakaoLoginData
import com.myongjiway.token.TokenType.*
import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.HS512
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.util.Date

class AuthServiceTest :
    FeatureSpec(
        {
            lateinit var userRepository: UserRepository
            lateinit var sut: AuthService
            lateinit var jwtProvider: JwtProvider
            lateinit var jwtProperty: JwtProperty

            val kakaoLoginData = KakaoLoginData(
                providerId = "1234",
                username = "test",
                profileImg = "test",
            )

            val user = User(
                id = 1,
                profileImg = "test",
                name = "test",
                providerId = "1234",
                providerType = ProviderType.KAKAO,
                role = Role.USER,
                createdAt = null,
                updatedAt = null,
            )

            beforeTest {
                userRepository = mockk()
                jwtProvider = mockk()
                jwtProperty = mockk()
                sut = AuthService(jwtProvider)

                every { jwtProperty.accessToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.accessToken.expiration } returns 1000
                every { jwtProperty.refreshToken.secret } returns "lnp1ISIafo9E+U+xZ4xr0kaRGD5uNVCT1tiJ8gXmqWvp32L7JoXC9EjAy0z2F6NVSwrKLxbCkpzT+DZJazy3Pg=="
                every { jwtProperty.refreshToken.expiration } returns 10000
                every { jwtProvider.generateAccessTokenByUserId(any()) } returns ACCESS.generate(
                    token = Jwts.builder()
                        .subject(kakaoLoginData.providerId)
                        .expiration(Date(System.currentTimeMillis() + jwtProperty.accessToken.expiration))
                        .signWith(Keys.hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray()), HS512)
                        .compact(),
                    expiration = Date(System.currentTimeMillis() + jwtProperty.accessToken.expiration),
                    userId = kakaoLoginData.providerId,
                )
                every { jwtProvider.generateRefreshTokenByUserId(any()) } returns REFRESH.generate(
                    token = Jwts.builder()
                        .subject(kakaoLoginData.providerId)
                        .expiration(Date(System.currentTimeMillis() + jwtProperty.refreshToken.expiration))
                        .signWith(Keys.hmacShaKeyFor(jwtProperty.refreshToken.secret.toByteArray()), HS512)
                        .compact(),
                    expiration = Date(System.currentTimeMillis() + jwtProperty.refreshToken.expiration),
                    userId = kakaoLoginData.providerId,
                )
            }

            feature("카카오 로그인") {
                scenario("입력 받은 providerId가 DB에 존재할 때 토큰을 반환한다.") {
                    // given
                    every { userRepository.findUserByProviderId(any()) } returns user

                    // when
                    val actual = sut.kakaoLogin(kakaoLoginData)

                    // then
                    actual.accessToken shouldNotBe null
                    actual.refreshToken shouldNotBe null
                }

                scenario("입력 받은 providerId가 DB에 존재하지 않으면 유저를 생성하고 토큰을 반환한다.") {
                    // given
                    every { userRepository.findUserByProviderId(any()) } returns null

                    // when
                    val actual = sut.kakaoLogin(kakaoLoginData)

                    // then
                    actual.accessToken shouldNotBe null
                    actual.refreshToken shouldNotBe null
                }
            }
        },
    )
