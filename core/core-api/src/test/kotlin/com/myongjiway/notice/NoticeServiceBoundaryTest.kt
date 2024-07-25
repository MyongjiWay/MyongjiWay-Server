package com.myongjiway.notice

import com.myongjiway.core.notice.domain.NoticeService
import com.myongjiway.core.notice.domain.NoticeServiceImpl
import com.myongjiway.domain.notice.Notice
import com.myongjiway.domain.notice.NoticeRepository
import com.myongjiway.storage.db.core.notice.NoticeEntity
import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk

class NoticeServiceBoundaryTest : FeatureSpec({
    lateinit var noticeRepository: NoticeRepository
    lateinit var noticeService: NoticeService

    beforeTest {
        noticeRepository = mockk()
        noticeService = NoticeServiceImpl(noticeRepository)
    }

    feature("공지사항 생성 - 경계값 테스트") {
        scenario("제목이 최대 길이 (100자)일 때") {
            val title = "a".repeat(100)
            val content = "Valid content"
            val notice= = Notice(title = title, content = content, author = "장호진")
            every { noticeRepository.save(any()) } returns notice

            val createdNotice = noticeService.createNotice(title, content, 1L)

            createdNotice.title shouldBe title
            createdNotice.content shouldBe content
            verify { noticeRepository.save(any<NoticeEntity>()) }
        }

        scenario("제목이 최소 길이 (1자)일 때") {
            val title = "a"
            val content = "Valid content"
            val noticeEntity = NoticeEntity(title = title, content = content, authorId = 1L)
            every { noticeRepository.save(any()) } returns noticeEntity

            val createdNotice = noticeService.createNotice(title, content, 1L)

            createdNotice.title shouldBe title
            createdNotice.content shouldBe content
            verify { noticeRepository.save(any<NoticeEntity>()) }
        }

        scenario("내용이 최대 길이 (1000자)일 때") {
            val title = "Valid title"
            val content = "a".repeat(1000)
            val noticeEntity = NoticeEntity(title = title, content = content, authorId = 1L)
            every { noticeRepository.save(any()) } returns noticeEntity

            val createdNotice = noticeService.createNotice(title, content, 1L)

            createdNotice.title shouldBe title
            createdNotice.content shouldBe content
            verify { noticeRepository.save(any<NoticeEntity>()) }
        }

        scenario("내용이 최소 길이 (1자)일 때") {
            val title = "Valid title"
            val content = "a"
            val noticeEntity = NoticeEntity(title = title, content = content, authorId = 1L)
            every { noticeRepository.save(any()) } returns noticeEntity

            val createdNotice = noticeService.createNotice(title, content, 1L)

            createdNotice.title shouldBe title
            createdNotice.content shouldBe content
            verify { noticeRepository.save(any<NoticeEntity>()) }
        }

        scenario("공지사항이 딱 한 개 존재할 때 목록 조회") {
            val notices = listOf(NoticeEntity(title = "Notice 1", content = "Content 1", authorId = 1L))
            every { noticeRepository.findAll() } returns notices

            val noticeList = noticeService.getAllNotices()

            noticeList.size shouldBe 1
            noticeList[0].title shouldBe "Notice 1"
            verify { noticeRepository.findAll() }
        }
    }
})