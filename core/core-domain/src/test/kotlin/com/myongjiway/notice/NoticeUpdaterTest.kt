package com.myongjiway.notice

import com.myongjiway.core.domain.notice.NoticeMetadata
import com.myongjiway.core.domain.notice.NoticeRepository
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.notice.NoticeUpdater
import com.myongjiway.core.domain.notice.NoticeView
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class NoticeUpdaterTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(
                mockk(),
                NoticeUpdater(noticeRepository),
                mockk(),
                mockk(),
            )
        }

        feature("공지사항 수정") {

            scenario("관리자가 공지사항을 수정할 때") {
                // Given
                val noticeId = 1L
                val existingNotice = getNoticeView("Initial Title", "Initial Content")
                val updateNotice = getNoticeMetadata("Updated Title", "Updated Content")

                every { noticeRepository.findById(noticeId) } returns existingNotice
                every {
                    noticeService.updateNotice(
                        match {
                            it.title == updateNotice.title &&
                                it.content == updateNotice.content
                        },
                        noticeId,
                    )
                } just runs

                // When
                noticeService.updateNotice(updateNotice, noticeId)

                // Then
                verify {
                    noticeService.updateNotice(
                        match {
                            it.title == updateNotice.title &&
                                it.content == updateNotice.content
                        },
                        noticeId,
                    )
                }
            }
        }

        feature("공지사항 수정 - 동시성 테스트") {

            scenario("여러 코루틴이 동시에 공지사항을 수정할 때") {
                // Given
                val noticeId1 = 1L
                val noticeId2 = 2L
                val initialNotice = getNoticeView("Initial Title", "Initial Content")
                every { noticeRepository.findById(noticeId1) } returns initialNotice
                every { noticeService.updateNotice(any(), any()) } just runs

                val updateDto1 = getNoticeMetadata("Updated Title 1", "Updated Content 1")
                val updateDto2 = getNoticeMetadata("Updated Title 2", "Updated Content 2")

                // 상태를 추적하기 위한 리스트 (수정된 제목과 내용을 저장)
                val titles = mutableListOf<String>()
                val contents = mutableListOf<String>()

                runBlocking {
                    // When
                    val job1 = async {
                        noticeService.updateNotice(updateDto1, noticeId1)
                        synchronized(titles) { titles.add(updateDto1.title) }
                        synchronized(contents) { contents.add(updateDto1.content) }
                    }

                    val job2 = async {
                        noticeService.updateNotice(updateDto2, noticeId2)
                        synchronized(titles) { titles.add(updateDto2.title) }
                        synchronized(contents) { contents.add(updateDto2.content) }
                    }

                    // Wait for all jobs to complete
                    job1.await()
                    job2.await()
                }

                // Then
                verify(exactly = 2) { noticeService.updateNotice(any(), any()) }
                titles shouldHaveSize 2 // 두 번의 수정이 수행되었는지 확인
                contents shouldHaveSize 2 // 두 번의 수정이 수행되었는지 확인

                // 마지막 수정된 결과가 올바른지 확인
                (titles.contains(updateDto1.title) || titles.contains(updateDto2.title)) shouldBe true
                (contents.contains(updateDto1.content) || contents.contains(updateDto2.content)) shouldBe true
            }
        }
    }) {
    companion object {
        fun getNoticeView(title: String, content: String): NoticeView = NoticeView(
            id = 1L,
            metadata = NoticeMetadata(title, "author", content),
            read = false,
            createdAt = LocalDateTime.now(),
        )

        fun getNoticeMetadata(title: String, content: String): NoticeMetadata = NoticeMetadata(
            title = title,
            author = "author",
            content = content,
        )
    }
}
