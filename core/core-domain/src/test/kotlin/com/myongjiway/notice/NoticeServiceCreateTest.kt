package com.myongjiway.notice

import com.myongjiway.error.CoreException
import com.myongjiway.user.Role
import com.myongjiway.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

class NoticeServiceCreateTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeServiceImpl(noticeRepository, mockk())
        }

        feature("공지사항 생성 - 인증/인가 관련 테스트") {

            scenario("권한이 없는 사용자가 공지사항을 생성하려고 시도할 때") {
                // Given
                val regularUser = User.fixture(1, role = Role.USER)
                val notice = Notice.fixture(title = "Title", content = "Content")

                // When
                val exception = shouldThrow<CoreException> {
                    noticeService.createNotice(notice, regularUser)
                }

                // Then
                exception.message shouldBe "권한이 없습니다."
            }

            scenario("관리자가 공지사항을 생성할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val noticeCreateRequest = Notice.fixture(title = "Title", content = "Content")

                // Mocking
                every { noticeRepository.save(any(), any()) } just Runs

                // When
                noticeService.createNotice(noticeCreateRequest, adminUser)

                // Then
                verify {
                    noticeRepository.save(
                        match { it == noticeCreateRequest.title },
                        match { it == noticeCreateRequest.content },
                    )
                }
            }
        }

        feature("공지사항 생성 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 생성할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val adminUser2 = User.fixture(2, role = Role.ADMIN)
                val noticeCreateRequest = Notice.fixture(title = "Title", content = "Content")

                val task1 = Runnable {
                    noticeService.createNotice(noticeCreateRequest, adminUser)
                }
                val task2 = Runnable {
                    noticeService.createNotice(noticeCreateRequest, adminUser2)
                }

                val thread1 = Thread(task1)
                val thread2 = Thread(task2)

                // When
                thread1.start()
                thread2.start()

                thread1.join()
                thread2.join()

                // Then
                verify(exactly = 2) { noticeRepository.save(any(), any()) }
            }
        }
    })
