package com.myongjiway.notice

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk

class NoticeServiceHappyCasesTest :
    FeatureSpec({
        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(noticeRepository)
        }

        feature("공지사항 생성") {
            scenario("정상적인 제목과 내용을 입력했을 때 공지사항을 생성하고 반환한다.") {
                val notice = Notice(title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.save(notice) } returns notice

                val createdNotice = noticeService.createNotice("Test Notice", "This is a test notice.")

                createdNotice.title shouldBe "Test Notice"
                createdNotice.content shouldBe "This is a test notice."
                verify { noticeRepository.save(notice) }
            }
        }

        feature("공지사항 조회") {
            scenario("존재하는 공지사항 ID로 조회했을 때 공지사항을 반환한다.") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.findById(1L) } returns Optional.of(notice)

                val foundNotice = noticeService.getNoticeById(1L)

                foundNotice.title shouldBe "Test Notice"
                foundNotice.content shouldBe "This is a test notice."
                verify { noticeRepository.findById(1L) }
            }
        }

        feature("공지사항 수정") {
            scenario("존재하는 공지사항을 정상적으로 수정했을 때 수정된 공지사항을 반환한다.") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.findById(1L) } returns Optional.of(notice)
                every { noticeRepository.save(any()) } returns notice

                val updatedNotice = noticeService.updateNotice(1L, "Updated Notice", "Updated content")

                updatedNotice.title shouldBe "Updated Notice"
                updatedNotice.content shouldBe "Updated content"
                verify { noticeRepository.save(any()) }
            }
        }

        feature("공지사항 삭제") {
            scenario("존재하는 공지사항 ID로 삭제했을 때 공지사항을 삭제한다.") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.findById(1L) } returns Optional.of(notice)
                every { noticeRepository.delete(notice) } returns Unit

                noticeService.deleteNotice(1L)

                verify { noticeRepository.delete(notice) }
            }
        }

        feature("공지사항 목록 조회") {
            scenario("여러 공지사항이 존재할 때 전체 목록을 조회한다.") {
                val notices = listOf(
                    Notice(id = 1L, title = "Notice 1", content = "Content 1"),
                    Notice(id = 2L, title = "Notice 2", content = "Content 2"),
                )
                every { noticeRepository.findAll() } returns notices

                val noticeList = noticeService.getAllNotices()

                noticeList.size shouldBe 2
                noticeList[0].title shouldBe "Notice 1"
                noticeList[1].title shouldBe "Notice 2"
                verify { noticeRepository.findAll() }
            }
        }
    })
