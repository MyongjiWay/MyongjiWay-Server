package com.myongjiway.notice

import com.myongjiway.error.CoreErrorType
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

class NoticeServiceDeleteTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeServiceImpl(noticeRepository, mockk())
        }

        feature("공지사항 삭제 - 인증/인가 관련 테스트") {

            scenario("권한이 없는 사용자가 공지사항을 삭제하려고 시도할 때") {
                // Given
                val regularUser = User.fixture(1, role = Role.USER)

                // When
                val exception = shouldThrow<CoreException> {
                    noticeService.deleteNotice(1, regularUser)
                }

                // Then
                exception.message shouldBe "권한이 없습니다."
            }

            scenario("관리자가 존재하지 않는 공지사항을 삭제하려고 시도할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                every { noticeRepository.delete(any()) } throws CoreException(CoreErrorType.NOT_FOUND_DATA)

                // When
                val exception = shouldThrow<CoreException> {
                    noticeService.deleteNotice(1, adminUser)
                }

                // Then
                exception.message shouldBe "해당 데이터를 찾지 못했습니다."
            }

            scenario("관리자가 공지사항을 삭제할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val existingNoticeId = 1L

                every { noticeRepository.delete(any()) } just Runs

                // When
                noticeService.deleteNotice(existingNoticeId, adminUser)

                // Then
                verify { noticeService.deleteNotice(match { it == existingNoticeId }, adminUser) }
            }
        }

        feature("공지사항 삭제 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 삭제할 때") {
                // Given
                val adminUser1 = User.fixture(1, role = Role.ADMIN)
                val adminUser2 = User.fixture(2, role = Role.ADMIN)

                every { noticeRepository.delete(any()) } just Runs

                val deleteId1 = 1L
                val deleteId2 = 2L

                val task1 = Runnable {
                    noticeService.deleteNotice(deleteId1, adminUser1)
                }
                val task2 = Runnable {
                    noticeService.deleteNotice(deleteId2, adminUser2)
                }

                val thread1 = Thread(task1)
                val thread2 = Thread(task2)

                thread1.start()
                thread2.start()

                thread1.join()
                thread2.join()

                // Then
                verify(exactly = 1) { noticeService.deleteNotice(match { it == deleteId1 }, adminUser1) }
                verify(exactly = 1) { noticeService.deleteNotice(match { it == deleteId2 }, adminUser2) }
            }

            scenario("여러 스레드가 동시에 동일한 공지사항을 삭제할 때 하나는 성공하고 하나는 실패하는 경우") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val adminUser2 = User.fixture(2, role = Role.ADMIN)

                // 초기 공지사항 설정
                val initialNotice = Notice.fixture(1, "Initial Title", "Initial Content")

                // 첫 번째 스레드가 삭제를 수행할 때는 성공적으로 삭제하고, 두 번째 스레드에서는 이미 삭제된 상태를 모의함
                every { noticeRepository.delete(any()) } just Runs andThenThrows CoreException(CoreErrorType.NOT_FOUND_DATA)

                val deleteId1 = 1L
                val deleteId2 = 2L

                val successes = mutableListOf<Boolean>()
                val exceptions = mutableListOf<Exception>()

                val task1 = Runnable {
                    try {
                        noticeService.deleteNotice(deleteId1, adminUser)
                        synchronized(successes) { successes.add(true) }
                    } catch (e: Exception) {
                        synchronized(exceptions) { exceptions.add(e) }
                    }
                }

                val task2 = Runnable {
                    try {
                        noticeService.deleteNotice(deleteId2, adminUser2)
                        synchronized(successes) { successes.add(true) }
                    } catch (e: Exception) {
                        synchronized(exceptions) { exceptions.add(e) }
                    }
                }

                val thread1 = Thread(task1)
                val thread2 = Thread(task2)

                thread1.start()
                thread2.start()

                thread1.join()
                thread2.join()

                // Then
                verify(atLeast = 1, atMost = 2) { noticeRepository.delete(any()) } // 삭제 시도가 최소 1번, 최대 2번 발생해야 함
                successes.size shouldBe 1 // 삭제 성공은 한 번 발생
                exceptions.size shouldBe 1 // 예외도 한 번 발생
                exceptions.first().message shouldBe "해당 데이터를 찾지 못했습니다." // 예외 메시지가 예상대로인지 확인
            }
        }
    })
