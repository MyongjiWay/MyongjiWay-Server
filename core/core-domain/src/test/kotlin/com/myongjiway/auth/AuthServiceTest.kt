package com.myongjiway.auth

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import com.myongjiway.user.User
import com.myongjiway.user.UserRepository
import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every

class AuthServiceTest :
    FeatureSpec(
        {
            lateinit var userRepository: UserRepository
            lateinit var sut: AuthService

            feature("카카오 로그인") {
                scenario("입력 받은 providerId가 DB에 존재할 때 토큰을 반환한다.") {
                    // given
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

                    every { userRepository.findUserByProviderId(kakaoLoginData.providerId) } returns user

                    // when
                    val actual = sut.kakaoLogin(kakaoLoginData)

                    // then
                }
            }
        },
    )
