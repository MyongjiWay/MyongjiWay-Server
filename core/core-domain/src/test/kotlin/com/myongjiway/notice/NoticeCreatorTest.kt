package com.myongjiway.notice

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.async

class NoticeCreatorTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(NoticeCreator(noticeRepository), mockk(), mockk(), mockk())
        }

        feature("공지사항 생성 테스트 (권한이 있다고 가정 없다면 Controller 에서 예외 처리") {

            scenario("관리자가 공지사항을 생성할 때") {
                // Given
                val noticeCreateRequest = Notice.fixture(title = "Title", content = "Content")

                // Mocking
                every { noticeRepository.save(any()) } just Runs

                // When
                noticeService.createNotice(noticeCreateRequest)

                // Then
                verify(exactly = 1) {
                    noticeRepository.save(
                        match {
                            it.title == noticeCreateRequest.title &&
                                it.content == noticeCreateRequest.content
                        },
                    )
                }
            }
        }

        feature("공지사항 생성 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 생성할 때 정상적으로 생성되어야 한다.") {
                // Given
                val noticeCreateRequest = Notice.fixture(title = "Title", content = "Content")

                every { noticeRepository.save(any()) } just runs

                // When
                val jobs = List(10) {
                    async {
                        noticeService.createNotice(noticeCreateRequest)
                    }
                }

                jobs.forEach { it.await() }

                // Then
                verify(exactly = 10) { noticeRepository.save(any()) }
            }
        }
    })
