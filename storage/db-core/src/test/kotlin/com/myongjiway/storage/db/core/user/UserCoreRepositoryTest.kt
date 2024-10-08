package com.myongjiway.storage.db.core.user

import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
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
            val userEntity = UserEntityProxy(
                id = 1000L,
                createdAt = mockk(),
                updatedAt = mockk(),
                profileImg = "img.url",
                name = "test",
                password = "test",
                providerId = "providerId",
                providerType = ProviderType.KAKAO,
                role = Role.USER,
            )

            beforeTest {
                userJpaRepository = mockk()
                sut = UserCoreRepository(userJpaRepository)
            }

            feature("유저 조회") {
                scenario("유저 ID로 유저를 조회한다") {
                    // given
                    every { userJpaRepository.findByIdAndIsDeleted(1000L, false) } returns userEntity

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
                scenario("Provider ID로 유저를 조회한다.") {
                    // given
                    val providerId = "providerId"
                    every { userJpaRepository.findByProviderIdAndIsDeleted(any(), false) } returns userEntity

                    // when
                    val actual = sut.findUserByProviderId(providerId)

                    // then
                    actual?.providerId shouldBe providerId
                }
                scenario("Provider ID 조회시 유저가 없으면 null을 반환한다") {
                    // given
                    val providerId = "12345678"
                    every { userJpaRepository.findByProviderIdAndIsDeleted(any(), false) } returns null

                    // when
                    val actual = sut.findUserByProviderId(providerId)

                    // then
                    actual shouldBe null
                }
                scenario("유저 이름으로 유저를 조회한다.") {
                    // given
                    val name = "test"
                    every { userJpaRepository.findByNameAndIsDeleted(any(), false) } returns userEntity

                    // when
                    val actual = sut.findUserByUsername(name)

                    // then
                    actual?.name shouldBe name
                }
                scenario("유저 이름 조회시 유저가 없으면 null을 반환한다") {
                    // given
                    val name = "test"
                    every { userJpaRepository.findByNameAndIsDeleted(any(), false) } returns null

                    // when
                    val actual = sut.findUserByUsername(name)

                    // then
                    actual shouldBe null
                }
            }

            feature("유저 추가") {
                scenario("유저를 추가한다") {
                    // given
                    val providerId = "providerId"
                    val profileImg = "img.url"
                    val name = "test"
                    val providerType = ProviderType.KAKAO
                    val role = Role.USER
                    every { userJpaRepository.save(any()) } returns userEntity

                    // when
                    val actual = sut.append(providerId, profileImg, name, providerType, role)

                    // then
                    actual shouldBe 1000L
                }
            }

            feature("유저 수정") {
                scenario("유저를 수정한다") {
                    // given
                    val providerId = "providerId"
                    val profileImg = "newImg.url"
                    val name = "newTest"
                    val role = Role.ADMIN
                    every { userJpaRepository.findByProviderIdAndIsDeleted(providerId, false) } returns userEntity

                    // when
                    val actual = sut.modify(providerId, profileImg, name, role)

                    // then
                    actual shouldBe 1000L
                }
                scenario("유저가 존재하지 않는다면 exception을 반환한다") {
                    // given
                    val providerId = "providerId"
                    val profileImg = "newImg.url"
                    val name = "newTest"
                    val role = Role.ADMIN
                    every { userJpaRepository.findByProviderIdAndIsDeleted(providerId, false) } returns null

                    // when
                    val actual = kotlin.runCatching { sut.modify(providerId, profileImg, name, role) }
                        .exceptionOrNull()

                    // then
                    actual.shouldBeInstanceOf<Exception>()
                }
            }

            feature("유저 추가 및 수정") {
                scenario("유저가 존재하지 않으면 추가한다") {
                    // given
                    val providerId = "providerId"
                    val profileImg = "img.url"
                    val name = "test"
                    val providerType = ProviderType.KAKAO
                    val role = Role.USER
                    every { userJpaRepository.findByProviderIdAndIsDeleted(providerId, false) } returns null
                    every { userJpaRepository.save(any()) } returns userEntity

                    // when
                    val actual = sut.upsert(providerId, profileImg, name, providerType, role)

                    // then
                    actual shouldBe 1000L
                }
                scenario("유저가 존재하면 수정한다") {
                    // given
                    val providerId = "providerId"
                    val profileImg = "newImg.url"
                    val name = "newTest"
                    val providerType = ProviderType.KAKAO
                    val role = Role.USER
                    every { userJpaRepository.findByProviderIdAndIsDeleted(providerId, false) } returns userEntity

                    // when
                    val actual = sut.upsert(providerId, profileImg, name, providerType, role)

                    // then
                    actual shouldBe 1000L
                }
            }

            feature("유저 비활성화") {
                scenario("유저를 비활성화에 성공한다.") {
                    // given
                    val providerId = "123123123"
                    every { userJpaRepository.findByProviderIdAndIsDeleted(providerId, false) } returns userEntity

                    // when
                    val actual = sut.inactive(providerId)

                    // then
                    actual shouldBe 1000L
                    userEntity.isDeleted shouldBe true
                }
            }
        },
    )
