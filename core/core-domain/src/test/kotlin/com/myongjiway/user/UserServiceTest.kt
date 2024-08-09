package com.myongjiway.user

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserServiceTest :
    FeatureSpec(
        {
            lateinit var userService: UserService
            lateinit var userUpdater: UserUpdater

            beforeTest {
                userUpdater = mockk()
                userService = UserService(userUpdater)
            }

            feature("유저 비활성화") {
                scenario("유저를 비활성화에 성공한다.") {
                    // given
                    every { userUpdater.inactive("123123123") } returns 1000L

                    // when
                    val actual = userService.inactive("123123123")

                    // then
                    verify(exactly = 1) { userUpdater.inactive(any()) }
                }
            }
        },
    )
