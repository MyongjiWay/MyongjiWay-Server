package com.myongjiway.notice

import com.myongjiway.error.CoreException
import com.myongjiway.user.Role
import com.myongjiway.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

class NoticeServiceUpdateTest :
    FeatureSpec({

        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeServiceImpl(noticeRepository, mockk())
        }

        feature("공지사항 수정 - 인증/인가 관련 테스트") {

            scenario("권한이 없는 사용자가 공지사항을 수정하려고 시도할 때") {
                // Given
                val regularUser = User.fixture(1, role = Role.USER)
                val noticeId = 1L
                // When
                val exception = shouldThrow<CoreException> {
                    noticeService.updateNotice(noticeId, Notice.fixture(title = "New Title", content = "New Content"), regularUser)
                }

                // Then
                exception.message shouldBe "권한이 없습니다."
            }

            scenario("관리자가 공지사항을 수정할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val noticeId = 1L
                val existingNotice = Notice.fixture(noticeId, "Title", "Content")

                every { noticeRepository.findById(noticeId) } returns existingNotice
                every { noticeRepository.update(noticeId, any<String>(), any<String>()) } just Runs

                val updateDto = Notice.fixture(noticeId, "Updated Title", "Updated Content")

                // When
                noticeService.updateNotice(noticeId, updateDto, adminUser)

                // Then
                verify {
                    noticeRepository.update(
                        match { it == noticeId },
                        match { it == updateDto.title },
                        match { it == updateDto.content },
                    )
                }
            }
        }

        feature("공지사항 수정 - 동시성 테스트") {

            scenario("여러 스레드가 동시에 공지사항을 수정할 때") {
                // Given
                val adminUser = User.fixture(1, role = Role.ADMIN)
                val adminUser2 = User.fixture(2, role = Role.ADMIN)

                // 초기 공지사항 설정
                val initialNotice = Notice.fixture(1, "Initial Title", "Initial Content")
                every { noticeRepository.findById(1) } returns initialNotice
                every { noticeRepository.update(any(), any(), any()) } just Runs

                val updateDto1 = Notice.fixture(1, "Updated Title 1", "Updated Content 1")
                val updateDto2 = Notice.fixture(1, "Updated Title 2", "Updated Content 2")

                // 상태를 추적하기 위한 리스트 (수정된 제목과 내용을 저장)
                val titles = mutableListOf<String>()
                val contents = mutableListOf<String>()

                val task1 = Runnable {
                    noticeService.updateNotice(updateDto1.id!!, updateDto1, adminUser)
                    synchronized(titles) { titles.add(updateDto1.title) }
                    synchronized(contents) { contents.add(updateDto1.content) }
                }
                val task2 = Runnable {
                    noticeService.updateNotice(updateDto2.id!!, updateDto2, adminUser2)
                    synchronized(titles) { titles.add(updateDto2.title) }
                    synchronized(contents) { contents.add(updateDto2.content) }
                }

                val thread1 = Thread(task1)
                val thread2 = Thread(task2)

                thread1.start()
                thread2.start()

                thread1.join()
                thread2.join()

                // Then
                verify(exactly = 2) { noticeRepository.update(any(), any(), any()) }
                titles.size shouldBe 2 // 두 번의 수정이 수행되었는지 확인
                contents.size shouldBe 2 // 두 번의 수정이 수행되었는지 확인

                // 마지막 수정된 결과가 올바른지 확인
                (titles.contains(updateDto1.title) || titles.contains(updateDto2.title)) shouldBe true
                (contents.contains(updateDto1.content) || contents.contains(updateDto2.content)) shouldBe true
            }
        }
    })
