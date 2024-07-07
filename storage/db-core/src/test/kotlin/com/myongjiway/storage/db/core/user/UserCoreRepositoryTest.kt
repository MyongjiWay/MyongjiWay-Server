package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType
import com.myongjiway.user.Role
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class UserCoreRepositoryTest :
    FeatureSpec(
        {
            lateinit var userJpaRepository: UserJpaRepository
            lateinit var sut: UserCoreRepository

            beforeTest {
                userJpaRepository = mockk()
                sut = UserCoreRepository(userJpaRepository)
            }

            feature("유저 조회") {
                scenario("유저 ID로 유저를 조회한다") {
                    // given
                    val userEntity = UserEntityProxy(
                        id = 1000L,
                        createdAt = mockk(),
                        updatedAt = mockk(),
                        profileImg = "profileImg",
                        name = "test",
                        providerId = "providerId",
                        providerType = ProviderType.KAKAO,
                        role = Role.USER,
                    )
                    every { userJpaRepository.findById(1000L) } returns Optional.of(userEntity)

                    // when
                    val actual = sut.findUserById(1000L)

                    // then
                    actual?.id shouldBe 1000L
                }

                scenario("유저 ID 조회시 유저가 없으면 exception을 반환한다") {
                    // given
                    every { userJpaRepository.findById(1000L) } returns Optional.empty()

                    // when
                    val actual = kotlin.runCatching { sut.findUserById(1000) }
                        .exceptionOrNull()

                    // then
                    actual.shouldBeInstanceOf<Exception>()
                }
            }
        },
    )
