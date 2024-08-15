package com.myongjiway.user

import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.UserRepository
import com.myongjiway.core.domain.user.UserUpdater
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

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

            feature("유저 비활성화") {
                scenario("유저 비활성화에 성공한다.") {
                    // given
                    val providerId = "123123123"
                    every { userRepository.inactive(providerId) } returns 1000L

                    // when
                    val actual = sut.inactive(providerId)

                    // then
                    actual shouldBe 1000L
                    verify(exactly = 1) { userRepository.inactive(any()) }
                }
            }
        },
    )
