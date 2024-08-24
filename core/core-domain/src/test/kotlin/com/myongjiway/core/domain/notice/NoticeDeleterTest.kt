package com.myongjiway.core.domain.notice

import com.myongjiway.core.domain.error.CoreErrorType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class NoticeDeleterTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: com.myongjiway.core.domain.notice.NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = com.myongjiway.core.domain.notice.NoticeService(
                mockk(),
                mockk(),
                com.myongjiway.core.domain.notice.NoticeDeleter(noticeRepository),
                mockk(),
            )
        }

        feature("공지사항 삭제 (권한이 있다고 가정 없다면 Controller 에서 예외 처리") {
            scenario("관리자가 존재하지 않는 공지사항을 삭제하려고 시도할 때") {
                // Given
                val noticeId = 1000L
                every { noticeRepository.delete(any()) } throws com.myongjiway.core.domain.error.CoreException(
                    CoreErrorType.NOT_FOUND_DATA,
                )

                // When
                val exception = shouldThrow<com.myongjiway.core.domain.error.CoreException> {
                    noticeService.deleteNotice(noticeId)
                }

                // Then
                exception.message shouldBe "해당 데이터를 찾지 못했습니다."
            }

            scenario("관리자가 공지사항을 삭제할 때") {
                // Given
                val existingNoticeId = 1L

                every { noticeRepository.delete(any()) } just Runs

                // When
                noticeService.deleteNotice(existingNoticeId)

                // Then
                verify { noticeService.deleteNotice(match { it == existingNoticeId }) }
            }
        }

        feature("공지사항 삭제 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 삭제할 때") {
                // Given
                every { noticeRepository.delete(any()) } just Runs

                val deleteIds = (1L..10L).toList()

                // When
                val jobs = deleteIds.map { id ->
                    async {
                        noticeService.deleteNotice(id)
                    }
                }

                jobs.forEach { it.await() }

                // Then
                verify(exactly = 10) { noticeRepository.delete(any()) }
            }

            scenario("여러 스레드가 동시에 동일한 공지사항을 삭제할 때 하나는 성공하고 하나는 실패하는 경우") {
                // Given
                every { noticeRepository.delete(any()) } just runs andThenThrows com.myongjiway.core.domain.error.CoreException(
                    CoreErrorType.NOT_FOUND_DATA,
                )

                val deleteId = 1L

                val successes = mutableListOf<Boolean>()
                val exceptions = mutableListOf<Exception>()

                // When
                runBlocking {
                    val job1 = async {
                        try {
                            noticeService.deleteNotice(deleteId)
                            synchronized(successes) { successes.add(true) }
                        } catch (e: Exception) {
                            synchronized(exceptions) { exceptions.add(e) }
                        }
                    }

                    val job2 = async {
                        try {
                            noticeService.deleteNotice(deleteId)
                            synchronized(successes) { successes.add(true) }
                        } catch (e: Exception) {
                            synchronized(exceptions) { exceptions.add(e) }
                        }
                    }

                    job1.await()
                    job2.await()
                }

                // Then
                verify(atLeast = 1, atMost = 2) { noticeRepository.delete(any()) }
                successes.size shouldBe 1
                exceptions.size shouldBe 1
                exceptions.first().message shouldBe "해당 데이터를 찾지 못했습니다."
            }
        }
    })
