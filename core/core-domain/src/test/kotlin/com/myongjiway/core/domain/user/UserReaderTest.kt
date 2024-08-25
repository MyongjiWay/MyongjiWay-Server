package com.myongjiway.core.domain.user

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserReaderTest :
    FeatureSpec(
        {
            lateinit var sut: UserReader
            lateinit var userRepository: UserRepository

            val user = User(
                id = 1000L,
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
                sut = UserReader(userRepository)
            }

            feature("유저 조회") {
                scenario("유저 ID로 유저를 조회한다") {
                    // given
                    every { userRepository.findUserById(1000L) } returns user

                    // when
                    val actual = sut.find(1000L)

                    // then
                    actual shouldBe user
                }
                scenario("유저 ID 조회시 유저가 없으면 USER_NOT_FOUND 에러를 반환한다") {
                    // given
                    every { userRepository.findUserById(1000L) } returns null

                    // when
                    val actual = kotlin.runCatching { sut.find(1000) }.exceptionOrNull()

                    // then
                    actual shouldBe CoreException(CoreErrorType.USER_NOT_FOUND)
                }
                scenario("유저 Provider ID로 유저를 조회한다") {
                    // given
                    every { userRepository.findUserByProviderId("1234") } returns user

                    // when
                    val actual = sut.find("1234")

                    // then
                    actual shouldBe user
                }
                scenario("유저 Provider ID 조회시 유저가 없으면 null을 반환한다") {
                    // given
                    every { userRepository.findUserByProviderId("1234") } returns null

                    // when
                    val actual = kotlin.runCatching { sut.find("1234") }.exceptionOrNull()

                    // then
                    actual shouldBe CoreException(CoreErrorType.USER_NOT_FOUND)
                }
                scenario("유저 name으로 유저를 조회한다") {
                    // given
                    every { userRepository.findUserByUsername("test") } returns user

                    // when
                    val actual = sut.findByUsername("test")

                    // then
                    actual shouldBe user
                }
                scenario("유저 name 조회시 유저가 없으면 null을 반환한다") {
                    // given
                    every { userRepository.findUserByUsername("test") } returns null

                    // when
                    val actual = kotlin.runCatching { sut.findByUsername("test") }.exceptionOrNull()

                    // then
                    actual shouldBe CoreException(CoreErrorType.USER_NOT_FOUND)
                }
            }
        },
    )
