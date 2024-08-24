package com.myongjiway.core.domain.user

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserAppenderTest :
    FeatureSpec(
        {
            lateinit var sut: UserAppender
            lateinit var userRepository: UserRepository

            beforeTest {
                userRepository = mockk()
                sut = UserAppender(userRepository)
            }
            feature("유저 추가") {
                scenario("유저를 추가한다") {
                    // given
                    val providerId = "1234"
                    val profileImg = "test"
                    val name = "test"
                    val providerType = ProviderType.KAKAO
                    val role = Role.USER
                    every { userRepository.append(providerId, profileImg, name, providerType, role) } returns 1000L

                    // when
                    val actual = sut.append(providerId, profileImg, name, providerType, role)

                    // then
                    actual shouldBe 1000L
                }
            }
            feature("유저 추가 및 수정") {
                scenario("유저가 없으면 추가하고 존재한다면 수정한다") {
                    // given
                    val providerId = "1234"
                    val profileImg = "test"
                    val name = "test"
                    val providerType = ProviderType.KAKAO
                    val role = Role.USER
                    every { userRepository.upsert(providerId, profileImg, name, providerType, role) } returns 1000L

                    // when
                    val actual = sut.upsert(providerId, profileImg, name, providerType, role)

                    // then
                    actual shouldBe 1000L
                }
            }
        },
    )
