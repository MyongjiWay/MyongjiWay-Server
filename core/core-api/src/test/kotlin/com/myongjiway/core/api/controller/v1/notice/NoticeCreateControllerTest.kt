package com.myongjiway.core.api.controller.v1.notice

import com.myongjiway.core.api.controller.NoticeAdminController
import com.myongjiway.core.api.controller.v1.request.NoticeRequest
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.time.LocalDateTime

class NoticeCreateControllerTest :
    FeatureSpec(
        {

            lateinit var noticeAdminController: NoticeAdminController
            lateinit var noticeService: NoticeService

            beforeTest {
                noticeService = mockk()
                noticeAdminController = NoticeAdminController(noticeService)
            }

            feature("공지사항 생성 - 유효성 검사 테스트") {
                // Given
                val user = getUser(Role.ADMIN)

                scenario("제목이 빈 문자열일 때") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "", author = "admin", content = "Content"), user)
                    }

                    // Then
                    exception.message shouldBe "제목은 비어있을 수 없습니다."
                }

                scenario("내용이 빈 문자열일 때") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "Title", author = "admin", content = ""), user)
                    }

                    // Then
                    exception.message shouldBe "내용은 비어있을 수 없습니다."
                }

                scenario("제목이 최대 길이를 초과할 때") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "a".repeat(101), author = "admin", content = "content"), user)
                    }

                    // Then
                    exception.message shouldBe "제목의 길이는 100자를 초과할 수 없습니다."
                }

                scenario("내용이 최대 길이를 초과할 때") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "title", author = "admin", content = "a".repeat(1001)), user)
                    }

                    // Then
                    exception.message shouldBe "내용의 길이는 1000자를 초과할 수 없습니다."
                }
            }

            feature("공지사항 생성 - 입력 데이터 검사") {
                // Given
                val user = getUser(Role.ADMIN)

                scenario("제목에 특수 문자만 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "@#$%^&*", author = "admin", content = "Content"), user)
                    }

                    // Then
                    exception.message shouldBe "특수 문자만 포함될 수 없습니다."
                }

                scenario("내용에 특수 문자만 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        // When
                        noticeAdminController.createNotice(NoticeRequest(title = "Title", author = "admin", content = "@#$%^&*"), user)
                    }

                    // Then
                    exception.message shouldBe "특수 문자만 포함될 수 없습니다."
                }

                scenario("제목에 숫자만 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "123456", author = "admin", content = "Content"), user)
                    }

                    // Then
                    exception.message shouldBe "숫자만 포함될 수 없습니다."
                }

                scenario("내용에 숫자만 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(NoticeRequest(title = "Title", author = "admin", content = "123456"), user)
                    }

                    // Then
                    exception.message shouldBe "숫자만 포함될 수 없습니다."
                }

                scenario("제목에 SQL Injection 시도가 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(
                            NoticeRequest(title = "DROP TABLE notices;", author = "admin", content = "Content"),
                            user,
                        )
                    }

                    // Then
                    exception.message shouldBe "SQL Injection이 포함되어 있습니다."
                }

                scenario("내용에 SQL Injection 시도가 포함된 경우") {
                    // When
                    val exception = shouldThrow<IllegalArgumentException> {
                        noticeAdminController.createNotice(
                            NoticeRequest(title = "Title", author = "admin", content = "DROP TABLE notices;"),
                            user,
                        )
                    }

                    // Then
                    exception.message shouldBe "SQL Injection이 포함되어 있습니다."
                }
            }
        },
    ) {
    companion object {
        fun getUser(role: Role) = User(
            id = 1L,
            profileImg = "profileImg",
            name = "name",
            providerId = "providerId",
            providerType = ProviderType.KAKAO,
            role = role,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
