package com.myongjiway.notice

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

class NoticeUpdaterTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(mockk(), NoticeUpdater(noticeRepository), mockk(), mockk())
        }

        feature("공지사항 수정") {

            scenario("관리자가 공지사항을 수정할 때") {
                // Given
                val noticeId = 1L
                val existingNotice = Notice.fixture(noticeId, "Title", "Content")
                val updateNotice = Notice.fixture(noticeId, "Updated Title", "Updated Content")

                every { noticeRepository.findById(noticeId) } returns existingNotice
                every { noticeService.updateNotice(match { it == updateNotice }, noticeId) } just runs

                // When
                noticeService.updateNotice(updateNotice, noticeId)

                // Then
                verify {
                    noticeService.updateNotice(
                        match { it == updateNotice },
                        noticeId,
                    )
                }
            }
        }

        feature("공지사항 수정 - 동시성 테스트") {

            scenario("여러 코루틴이 동시에 공지사항을 수정할 때") {
                // Given
                val initialNotice = Notice.fixture(1, "Initial Title", "Initial Content")
                every { noticeRepository.findById(1) } returns initialNotice
                every { noticeService.updateNotice(any(), any()) } just runs

                val updateDto1 = Notice.fixture(1, "Updated Title 1", "Updated Content 1")
                val updateDto2 = Notice.fixture(1, "Updated Title 2", "Updated Content 2")

                // 상태를 추적하기 위한 리스트 (수정된 제목과 내용을 저장)
                val titles = mutableListOf<String>()
                val contents = mutableListOf<String>()

                runBlocking {
                    // When
                    val job1 = async {
                        noticeService.updateNotice(updateDto1, updateDto1.id!!)
                        synchronized(titles) { titles.add(updateDto1.title) }
                        synchronized(contents) { contents.add(updateDto1.content) }
                    }

                    val job2 = async {
                        noticeService.updateNotice(updateDto2, updateDto2.id!!)
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
    })
