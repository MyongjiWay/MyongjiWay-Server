package com.myongjiway.notice

import com.myongjiway.core.notice.controller.NoticeController
import com.myongjiway.core.notice.controller.v1.request.NoticeRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class NoticeUpdateControllerTest :
    FeatureSpec({

        lateinit var noticeController: NoticeController
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeService = mockk()
            noticeController = NoticeController(noticeService)
        }

        feature("공지사항 수정 - 유효성 검사 테스트") {

            scenario("제목이 빈 문자열일 때") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "", content = "Content"))
                }

                // Then
                exception.message shouldBe "제목은 비어있을 수 없습니다."
            }

            scenario("내용이 빈 문자열일 때") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "Title", content = ""))
                }

                // Then
                exception.message shouldBe "내용은 비어있을 수 없습니다."
            }

            scenario("제목이 최대 길이를 초과할 때") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "a".repeat(101), content = "content"))
                }

                // Then
                exception.message shouldBe "제목의 길이는 100자를 초과할 수 없습니다."
            }

            scenario("내용이 최대 길이를 초과할 때") {
                // Given

                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "title", content = "a".repeat(1001)))
                }

                // Then
                exception.message shouldBe "내용의 길이는 1000자를 초과할 수 없습니다."
            }
        }

        feature("공지사항 수정 - 입력 데이터 검사") {

            scenario("제목에 특수 문자만 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "@#$%^&*", content = "Content"))
                }

                // Then
                exception.message shouldBe "특수 문자만 포함될 수 없습니다."
            }

            scenario("내용에 특수 문자만 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "Title", content = "@#$%^&*"))
                }

                // Then
                exception.message shouldBe "특수 문자만 포함될 수 없습니다."
            }

            scenario("제목에 숫자만 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "123456", content = "Content"))
                }

                // Then
                exception.message shouldBe "숫자만 포함될 수 없습니다."
            }

            scenario("내용에 숫자만 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "Title", content = "123456"))
                }

                // Then
                exception.message shouldBe "숫자만 포함될 수 없습니다."
            }

            scenario("제목에 SQL Injection 시도가 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "DROP TABLE notices;", content = "Content"))
                }

                // Then
                exception.message shouldBe "SQL Injection이 포함되어 있습니다."
            }

            scenario("내용에 SQL Injection 시도가 포함된 경우") {
                // Given
                val exception = shouldThrow<IllegalArgumentException> {
                    // When
                    noticeController.updateNotice(1, NoticeRequest(title = "Title", content = "DROP TABLE notices;"))
                }

                // Then
                exception.message shouldBe "SQL Injection이 포함되어 있습니다."
            }
        }
    })
