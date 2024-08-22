package com.myongjiway.core.domain.notice

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
            noticeService = NoticeService(
                NoticeCreator(noticeRepository),
                mockk(),
                mockk(),
                mockk(),
            )
        }

        feature("공지사항 생성 테스트 (권한이 있다고 가정 없다면 Controller 에서 예외 처리") {

            scenario("관리자가 공지사항을 생성할 때") {
                // Given
                val noticeMetadata = getNotice("Title", "Content")

                // Mocking
                every { noticeRepository.save(any()) } just Runs

                // When
                noticeService.createNotice(noticeMetadata)

                // Then
                verify(exactly = 1) {
                    noticeRepository.save(
                        match {
                            it.title == noticeMetadata.title &&
                                it.content == noticeMetadata.content
                        },
                    )
                }
            }
        }

        feature("공지사항 생성 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 생성할 때 정상적으로 생성되어야 한다.") {
                // Given
                val noticeMetaData = getNotice("Title", "Content")

                every { noticeRepository.save(any()) } just runs

                // When
                val jobs = List(10) {
                    async {
                        noticeService.createNotice(noticeMetaData)
                    }
                }

                jobs.forEach { it.await() }

                // Then
                verify(exactly = 10) { noticeRepository.save(any()) }
            }
        }
    }) {
    companion object {
        fun getNotice(title: String, content: String): NoticeMetadata = NoticeMetadata(
            title = title,
            author = "author",
            content = content,
        )
    }
}
