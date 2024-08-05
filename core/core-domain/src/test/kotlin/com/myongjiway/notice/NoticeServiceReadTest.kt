package com.myongjiway.notice

import com.myongjiway.user.User
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NoticeServiceReadTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var userNoticeRepository: UserNoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            userNoticeRepository = mockk()
            noticeService = NoticeServiceImpl(noticeRepository, userNoticeRepository)
        }

        feature("공지사항 조회 - 기본 기능 테스트") {

            scenario("공지사항이 없는 경우 조회") {
                // Given
                val user = User.fixture(1)
                every { noticeRepository.findAll() } returns emptyList()
                every { userNoticeRepository.findByUserId(user.id!!) } returns emptyList()

                // When
                val result = noticeService.getNotices(user)

                // Then
                result shouldBe emptyList()
            }

            scenario("공지사항을 읽은 기록이 없는 경우 조회") {
                // Given
                val user = User.fixture(1)
                val notices = listOf(
                    Notice.fixture(1, "Notice 1", "Content 1"),
                    Notice.fixture(2, "Notice 2", "Content 2"),
                )

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(user.id!!) } returns emptyList()

                // When
                val result = noticeService.getNotices(user)

                // Then
                result.size shouldBe 2
                result.forEach { it.read shouldBe false }
            }
        }

        feature("공지사항 조회 - 읽음 상태 확인 테스트") {

            scenario("모든 공지사항이 읽음 상태일 때 조회") {
                // Given
                val user = User.fixture(1)
                val notices = listOf(
                    Notice.fixture(1, "Notice 1", "Content 1"),
                    Notice.fixture(2, "Notice 2", "Content 2"),
                )
                val readNotices = listOf(1L, 2L)

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(user.id!!) } returns readNotices.map { noticeId ->
                    UserNotice.fixture(noticeId = noticeId)
                }

                // When
                val result = noticeService.getNotices(user)

                // Then
                result.forEach { it.read shouldBe true }
            }

            scenario("특정 공지사항만 읽은 경우 조회") {
                // Given
                val user = User.fixture(1)
                val notices = listOf(
                    Notice.fixture(1, "Notice 1", "Content 1"),
                    Notice.fixture(2, "Notice 2", "Content 2"),
                    Notice.fixture(3, "Notice 3", "Content 3"),
                )
                val readNotices = listOf(1L, 2L)

                every { noticeRepository.findAll() } returns notices
                every { userNoticeRepository.findByUserId(user.id!!) } returns readNotices.map { noticeId ->
                    UserNotice.fixture(noticeId = noticeId)
                }

                // When
                val result = noticeService.getNotices(user)

                // Then
                result[0].read shouldBe true
                result[1].read shouldBe true
                result[2].read shouldBe false
            }
        }

        feature("공지사항 단건 조회 테스트") {

            scenario("특정 공지사항을 읽고 읽음 상태를 업데이트") {
                // Given
                val user = User.fixture(1)
                val notice = Notice.fixture(1, "Notice 1", "Content 1")
                val noticeId = 1L

                every { noticeRepository.findById(noticeId) } returns notice
                every { userNoticeRepository.findByUserIdAndNoticeId(user.id!!, noticeId) } returns null
                every { userNoticeRepository.save(any(), user.id!!) } returns UserNotice.fixture(user.id!!, noticeId)

                // When
                val result = noticeService.getNotice(noticeId, user)

                // Then
                result.read shouldBe true

                verify { userNoticeRepository.save(any(), user.id!!) }
            }

            scenario("이미 읽은 공지사항을 다시 조회할 때 중복된 읽음 상태가 업데이트되지 않음") {
                // Given
                val user = User.fixture(1)
                val notice = Notice.fixture(1, "Notice 1", "Content 1")
                val noticeId = 1L

                every { noticeRepository.findById(noticeId) } returns notice
                every { userNoticeRepository.findByUserIdAndNoticeId(user.id!!, noticeId) } returns UserNotice.fixture(user.id!!, noticeId)

                // When
                val result = noticeService.getNotice(noticeId, user)

                // Then
                result.read shouldBe true

                // Verify that save is not called again
                verify(exactly = 0) { userNoticeRepository.save(any(), any()) }
            }
        }

        feature("공지사항 조회 - 비동기 테스트") {

            scenario("비동기 동작으로 여러 사용자가 같은 공지사항을 다르게 조회") {
                // Given
                val user1 = User.fixture(1)
                val user2 = User.fixture(2)
                val notice = Notice.fixture(1, "Notice 1", "Content 1")

                every { noticeRepository.findById(notice.id!!) } returns notice
                every { noticeRepository.findAll() } returns listOf(notice)
                every { userNoticeRepository.findByUserIdAndNoticeId(user1.id!!, notice.id!!) } returns null
                every { userNoticeRepository.findByUserIdAndNoticeId(user2.id!!, notice.id!!) } returns null
                every { userNoticeRepository.save(any(), any()) } answers { firstArg() }

                // When
                val task1 = Runnable {
                    noticeService.getNotice(1L, user1)
                }
                val task2 = Runnable {
                    val result = noticeService.getNotices(user2)
                    result.forEach { it.read shouldBe false }
                }

                val thread1 = Thread(task1)
                val thread2 = Thread(task2)

                thread1.start()
                thread2.start()

                thread1.join()
                thread2.join()

                // Then
                verify(exactly = 1) { userNoticeRepository.save(any(), user1.id!!) }
                verify(exactly = 0) { userNoticeRepository.save(any(), user2.id!!) }
            }
        }
    })
