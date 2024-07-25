package com.myongjiway.notice

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class NoticeServiceErrorTest :
    FeatureSpec({
        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(noticeRepository)
        }

        feature("공지사항 조회 - 에러 케이스") {
            scenario("존재하지 않는 공지사항 ID로 조회 시도") {
                every { noticeRepository.findById(999L) } returns Optional.empty()

                val exception = shouldThrow<NoticeNotFoundException> {
                    noticeService.getNoticeById(999L)
                }

                exception.message shouldBe "Notice not found with id 999"
            }

            scenario("제목이 공백 문자만 포함하는 경우") {
                val title = "   "
                val content = "Valid content"

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.createNotice(title, content)
                }

                exception.message shouldBe "Title cannot be blank"
            }

            scenario("내용이 공백 문자만 포함하는 경우") {
                val title = "Valid title"
                val content = "   "

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.createNotice(title, content)
                }

                exception.message shouldBe "Content cannot be blank"
            }
        }

        feature("공지사항 수정 - 에러 케이스") {
            scenario("존재하지 않는 공지사항 ID로 수정 시도") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.findById(1L) } returns Optional.of(notice)

                val exception = shouldThrow<NoticeNotFoundException> {
                    noticeService.updateNotice(999L, "Updated Notice", "Updated content")
                }

                exception.message shouldBe "Notice not found with id 999"
            }

            scenario("제목이 공백 문자만 포함하는 경우") {
                val title = "   "
                val content = "Valid content"

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.updateNotice(1L, title, content)
                }

                exception.message shouldBe "Title cannot be blank"
            }

            scenario("내용이 공백 문자만 포함하는 경우") {
                val title = "Valid title"
                val content = "   "

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.updateNotice(1L, title, content)
                }

                exception.message shouldBe "Content cannot be blank"
            }
        }

        feature("공지사항 삭제 - 에러 케이스") {
            scenario("존재하지 않는 공지사항 ID로 삭제 시도") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.")
                every { noticeRepository.findById(999L) } returns Optional.empty()

                val exception = shouldThrow<NoticeNotFoundException> {
                    noticeService.deleteNotice(999L)
                }

                exception.message shouldBe "Notice not found with id 999"
            }
        }
    })
