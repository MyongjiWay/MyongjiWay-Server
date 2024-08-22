package com.myongjiway.core.domain.notice

import com.myongjiway.core.domain.usernotice.UserNotice
import com.myongjiway.core.domain.usernotice.UserNoticeRepository
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class NoticeFinderTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var userNoticeRepository: UserNoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            userNoticeRepository = mockk()
            noticeService = NoticeService(
                mockk(),
                mockk(),
                mockk(),
                NoticeFinder(noticeRepository, userNoticeRepository),
            )
        }

        feature("공지사항 조회 - 기본 기능 테스트") {

            scenario("공지사항이 없는 경우 조회") {
                // Given
                val userId = 1000L
                every { noticeRepository.findAll() } returns emptyList()
                every { userNoticeRepository.findByUserId(userId) } returns emptyList()

                // When
                val result = noticeService.getNotices(userId)

                // Then
                result shouldBe emptyList()
            }

            scenario("공지사항을 읽은 기록이 없는 경우 조회") {
                // Given
                val userId = 1000L
                val notices = listOf(
                    getNotice(1L, "Notice 1", "Content 1", false),
                    getNotice(2L, "Notice 2", "Content 2", false),
                )

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(userId) } returns emptyList()

                // When
                val result = noticeService.getNotices(userId)

                // Then
                result.size shouldBe 2
                result.forEach { it.read shouldBe false }
            }
        }

        feature("공지사항 조회 - 읽음 상태 확인 테스트") {
            scenario("모든 공지사항이 읽음 상태일 때 조회") {
                // Given
                val userId = 1000L
                val notices = listOf(
                    getNotice(1L, "Notice 1", "Content 1", false),
                    getNotice(2L, "Notice 2", "Content 2", false),
                )
                val readNotices = listOf(1L, 2L)

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(userId) } returns readNotices.map { noticeId ->
                    UserNotice.fixture(noticeId = noticeId)
                }

                // When
                val result = noticeService.getNotices(userId)

                // Then
                result.forEach { it.read shouldBe true }
            }

            scenario("특정 공지사항만 읽은 경우 조회") {
                // Given
                val userId = 1000L
                val notices = listOf(
                    getNotice(1L, "Notice 1", "Content 1", true),
                    getNotice(2L, "Notice 2", "Content 2", true),
                    getNotice(3L, "Notice 3", "Content 3", false),
                )
                val readNotices = listOf(1L, 2L)

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(userId) } returns readNotices.map { noticeId ->
                    UserNotice.fixture(noticeId = noticeId)
                }

                // When
                val result = noticeService.getNotices(userId)

                // Then
                result[0].read shouldBe true
                result[1].read shouldBe true
                result[2].read shouldBe false
            }
        }

        feature("공지사항 단건 조회 테스트") {
            val notice = getNotice(1, "Notice 1", "Content 1", false)

            scenario("특정 공지사항을 읽고 읽음 상태를 업데이트") {
                // Given
                val userId = 1000L
                val noticeId = 1L

                every { noticeRepository.findById(noticeId) } returns notice
                every { userNoticeRepository.findByUserIdAndNoticeId(userId, noticeId) } returns null
                every { userNoticeRepository.save(any(), userId) } returns UserNotice.fixture(userId, noticeId)

                // When
                val result = noticeService.getNotice(noticeId, userId)

                // Then
                result.read shouldBe true

                verify {
                    noticeRepository.findById(noticeId) // Notice 조회 확인
                    userNoticeRepository.findByUserIdAndNoticeId(userId, noticeId) // 사용자 읽음 여부 조회 확인
                    userNoticeRepository.save(any(), userId) // 읽음 상태 저장 확인
                }
            }

            scenario("이미 읽은 공지사항을 다시 조회할 때 중복된 읽음 상태가 업데이트되지 않음") {
                // Given
                val userId = 1000L
                val noticeId = 1L

                every { noticeRepository.findById(noticeId) } returns notice // Notice 조회 설정
                every { userNoticeRepository.findByUserIdAndNoticeId(userId, noticeId) } returns UserNotice.fixture(userId, noticeId) // 이미 읽음 상태 설정

                // When
                val result = noticeService.getNotice(noticeId, userId)

                // Then
                result.read shouldBe true

                verify(exactly = 0) { userNoticeRepository.save(any(), any()) }
            }
        }

        feature("공지사항 조회 - 비동기 테스트") {
            val notice = getNotice(1L, "Notice 1", "Content 1", false)

            scenario("비동기 동작으로 여러 사용자가 같은 공지사항을 다르게 조회") {
                // Given
                val userId = 1000L
                val userId2 = 10001L

                every { noticeRepository.findById(notice.id) } returns notice
                every { noticeRepository.findAll() } returns listOf(notice)
                every { userNoticeRepository.findByUserIdAndNoticeId(userId, notice.id) } returns null
                every { userNoticeRepository.findByUserIdAndNoticeId(userId2, notice.id) } returns null
                every { userNoticeRepository.findByUserId(any()) } returns emptyList()
                every { userNoticeRepository.save(any(), any()) } answers {
                    UserNotice.fixture(firstArg(), secondArg()) // 올바른 타입의 객체를 반환
                }

                runBlocking {
                    // When
                    val job1 = launch {
                        noticeService.getNotice(notice.id, userId)
                    }
                    val job2 = launch {
                        val result = noticeService.getNotices(userId)
                        result.forEach { it.read shouldBe false }
                    }

                    // Wait for all jobs to complete
                    job1.join()
                    job2.join()
                }

                // Then
                verify(exactly = 1) { userNoticeRepository.save(any(), userId) }
                verify(exactly = 0) { userNoticeRepository.save(any(), userId2) }
            }
        }
    }) {
    companion object {
        fun getNotice(id: Long, title: String, content: String, read: Boolean): NoticeView = NoticeView(
            id = id,
            metadata = NoticeMetadata(title, "author", content),
            read = read,
            createdAt = LocalDateTime.now(),
        )
    }
}
