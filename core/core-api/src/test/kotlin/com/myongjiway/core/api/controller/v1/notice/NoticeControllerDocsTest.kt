package com.myongjiway.core.api.controller.v1.notice

import com.myongjiway.core.api.controller.NoticeController
import com.myongjiway.core.domain.notice.NoticeMetadata
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.notice.NoticeView
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import java.time.LocalDateTime

class NoticeControllerDocsTest : RestDocsTest() {
    private lateinit var noticeService: NoticeService
    private lateinit var controller: NoticeController

    @BeforeEach
    fun setUp() {
        noticeService = mockk()
        controller = NoticeController(noticeService)
        mockMvc = mockController(controller)
    }

    @Test
    fun getNotice() {
        val noticeId = 1L
        val noticeResponse = getNoticeView(
            noticeId,
            "Existing Notice",
            "Notice Content",
            false,
        )

        every { noticeService.getNotice(any(), any()) } returns noticeResponse

        given()
            .get("/api/v1/notices/{noticeId}", noticeId.toString())
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "getNotice",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("noticeId").description("조회할 공지사항 ID"),
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과 상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("공지사항 ID"),
                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("공지사항 작성자"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("공지사항 내용"),
                        fieldWithPath("data.read").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("공지사항 작성일"),
                        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
                    ),
                ),
            )
    }

    @Test
    fun listNotices() {
        val notices = listOf(
            getNoticeView(
                id = 1,
                title = "First Existing Notice",
                content = "First Notice Content",
                read = false,
            ),
            getNoticeView(
                id = 2,
                title = "Second Existing Notice",
                content = "Second Notice Content",
                read = false,
            ),
        )

        every { noticeService.getNotices(any()) } returns notices

        given()
            .get("/api/v1/notices")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "listNotices",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("응답 결과 상태"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("공지사항 ID"),
                        fieldWithPath("data[].author").type(JsonFieldType.STRING).description("공지사항 작성자"),
                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("data[].content").type(JsonFieldType.STRING).description("공지사항 내용"),
                        fieldWithPath("data[].read").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("공지사항 작성일"),
                        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
                    ),
                ),
            )
    }
    companion object {
        fun getNoticeView(id: Long, title: String, content: String, read: Boolean): NoticeView = NoticeView(
            id = id,
            metadata = NoticeMetadata(title, "장호진", content),
            read = read,
            createdAt = LocalDateTime.now(),
        )
    }
}
