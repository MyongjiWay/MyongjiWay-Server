package com.myongjiway.notice

import com.myongjiway.core.notice.domain.NoticeService
import com.myongjiway.core.notice.domain.NoticeServiceImpl
import com.myongjiway.domain.notice.Notice
import com.myongjiway.domain.notice.NoticeRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.mockk.mockk

class NoticeServiceAuthTest :
    FeatureSpec({
        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeServiceImpl(noticeRepository)
        }

        feature("공지사항 생성 - 인증/인가 관련 테스트") {
            scenario("권한이 없는 사용자가 공지사항을 생성하려고 시도할 때") {
                val notice = Notice(title = "Test Notice", content = "This is a test notice.", authorId = 1L)

                val exception = shouldThrow<AuthorizationException> {
                    noticeService.createNotice("Test Notice", "This is a test notice.", 1L, "USER")
                }

                exception.message shouldBe "User not authorized"
            }
        }

        feature("공지사항 수정 - 인증/인가 관련 테스트") {
            scenario("권한이 없는 사용자가 공지사항을 수정하려고 시도할 때") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.", authorId = 1L)
                every { noticeRepository.findById(1L) } returns Optional.of(notice)

                val exception = shouldThrow<AuthorizationException> {
                    noticeService.updateNotice(1L, "Updated Notice", "Updated content", "USER")
                }

                exception.message shouldBe "User not authorized"
            }
        }

        feature("공지사항 삭제 - 인증/인가 관련 테스트") {
            scenario("권한이 없는 사용자가 공지사항을 삭제하려고 시도할 때") {
                val notice = Notice(id = 1L, title = "Test Notice", content = "This is a test notice.", authorId = 1L)
                every { noticeRepository.findById(1L) } returns Optional.of(notice)

                val exception = shouldThrow<AuthorizationException> {
                    noticeService.deleteNotice(1L, "USER")
                }

                exception.message shouldBe "User not authorized"
            }
        }
    })
