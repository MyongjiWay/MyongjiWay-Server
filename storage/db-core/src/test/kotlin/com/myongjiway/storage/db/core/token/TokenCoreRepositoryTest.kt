package com.myongjiway.storage.db.core.token

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TokenCoreRepositoryTest :
    FeatureSpec(
        {
            lateinit var sut: TokenCoreRepository
            lateinit var tokenJpaRepository: TokenJpaRepository
            val tokenEntity = TokenEntityProxy(
                id = 1000L,
                createdAt = mockk(),
                updatedAt = mockk(),
                userId = 123L,
                token = "token",
                expiration = 1721041381000,
            )

            beforeTest {
                tokenJpaRepository = mockk()
                sut = TokenCoreRepository(tokenJpaRepository)
            }

            afterTest {
                tokenEntity.token = "token"
            }

            feature("토큰이 존재하지 않으면 새로 생성한고 존재하면 업데이트한다.") {
                scenario("토큰이 존재하지 않으면 새로 생성하고 저장에 성공한다.") {
                    // given
                    every { tokenJpaRepository.findByUserId(123) } returns null
                    every { tokenJpaRepository.save(any()) } returns tokenEntity

                    // when
                    sut.upsert(123, "token", 1721041381000)

                    // then
                    verify(exactly = 1) { tokenJpaRepository.save(any()) }
                }

                scenario("토큰이 존재하면 업데이트에 성공한다.") {
                    // given
                    every { tokenJpaRepository.findByUserId(123) } returns tokenEntity
                    every { tokenJpaRepository.save(any()) } returns tokenEntity

                    // when
                    sut.upsert(123, "new token", 1721041381000)

                    // then
                    verify(exactly = 0) { tokenJpaRepository.save(any()) }
                }
            }

            feature("토큰 조회") {
                scenario("토큰이 존재하면 조회에 성공한다.") {
                    // given
                    every { tokenJpaRepository.findByToken(any()) } returns tokenEntity

                    // when
                    val actual = sut.find("token")

                    // then
                    actual?.token shouldBe "token"
                }

                scenario("토큰이 존재하지 않으면 null을 반환한다.") {
                    // given
                    every { tokenJpaRepository.findByToken(any()) } returns null

                    // when
                    val actual = sut.find("token")

                    // then
                    actual shouldBe null
                }
            }
        },
    )
