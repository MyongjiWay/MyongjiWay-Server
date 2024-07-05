package com.myongjiway.storage.db.core.user

import com.myongjiway.user.ProviderType
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

class UserCoreRepositoryTest :
    FeatureSpec(
        {
            lateinit var sut: UserCoreRepository
            lateinit var userJpaRepository: UserJpaRepository

            beforeTest {
                sut = UserCoreRepository(userJpaRepository)
            }

            feature("유저 조회") {
                scenario("유저 ID로 유저를 조회한다") {
                    // given
                    val userEntity = UserEntity(
                        profileImg = "profileImg",
                        name = "name",
                        providerId = "providerId",
                        providerType = ProviderType.KAKAO,
                    )
                    val savedUser = userJpaRepository.save(userEntity)

                    // when
                    val user = sut.findUserById(userEntity.id!!)

                    // then
                    user shouldBe userEntity.toUser()
                }
            }
        },
    )
