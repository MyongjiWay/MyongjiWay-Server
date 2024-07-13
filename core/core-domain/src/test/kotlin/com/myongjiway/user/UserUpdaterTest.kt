package com.myongjiway.user

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserUpdaterTest :
    FeatureSpec(
        {
            lateinit var sut: UserUpdater
            lateinit var userRepository: UserRepository

            beforeTest {
                userRepository = mockk()
                sut = UserUpdater(userRepository)
            }

            feature("유저 정보 수정") {
                scenario("유저 정보 수정 성공") {
                    // given
                    every { userRepository.modify(any(), any(), any(), any()) } returns 1L
                    val providerId = "providerId"
                    val profileImg = "profileImg"
                    val name = "name"
                    val role = Role.USER

                    // when
                    val actual = sut.modify(providerId, profileImg, name, role)

                    // then
                    actual shouldBe 1L
                }
            }
        },
    )
