package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
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
                        profileImg = "profileImg",
                        name = "test",
                        providerId = "providerId",
                        providerType = ProviderType.KAKAO,
                    )
                    every { userJpaRepository.findById(1000L) } returns Optional.of(userEntity)

                    // when
                    val actual = sut.findUserById(1000)

                    // then
                    actual?.id shouldBe 1000L
                }
            }
        },
    )
