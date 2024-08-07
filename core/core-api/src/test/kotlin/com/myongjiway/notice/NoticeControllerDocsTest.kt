package com.myongjiway.notice

import com.myongjiway.core.notice.controller.NoticeController
import com.myongjiway.core.notice.controller.v1.request.NoticeRequest
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
import com.myongjiway.user.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters

class NoticeControllerDocsTest : RestDocsTest() {
    private lateinit var noticeService: NoticeService
    private lateinit var controller: NoticeController
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        noticeService = mockk() // Mocking the service
        controller = NoticeController(noticeService) // Initializing the controller
        mockMvc = mockController(controller) // Setting up MockMvc with the controller
        user = User.fixture(1) // Initialize user mock object
    }

    @Test
    fun createNotice() {
        val noticeRequest = NoticeRequest("New Notice", "Notice Content")

        every { noticeService.createNotice(any()) } just Runs

        given()
            .contentType(ContentType.JSON)
            .body(noticeRequest)
            .post("/api/v1/domain/notices")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "createNotice",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용"),
                    ),
                ),
            )
    }

    @Test
    fun updateNotice() {
        val noticeId = 1L
        val noticeRequest = NoticeRequest("Updated Notice", "Updated Content")

        every { noticeService.updateNotice(any(), any()) } just Runs

        given()
            .contentType(ContentType.JSON)
            .body(noticeRequest)
            .put("/api/v1/domain/notices/{noticeId}", noticeId.toString())
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "updateNotice",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("noticeId").description("업데이트 할 공지사항 ID"),
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용"),
                    ),
                ),
            )
    }

    @Test
    fun deleteNotice() {
        val noticeId = 1L

        every { noticeService.deleteNotice(any()) } just Runs

        given()
            .delete("/api/v1/domain/notices/{noticeId}", noticeId.toString())
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "deleteNotice",
                    pathParameters(
                        parameterWithName("noticeId").description("삭제할 공지사항 ID"),
                    ),
                ),
            )
    }

    @Test
    fun getNotice() {
        val noticeId = 1L
        val noticeResponse = Notice.fixture(noticeId, "Existing Notice", "Notice Content", read = true)

        every { noticeService.getNotice(any()) } returns noticeResponse

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
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("공지사항 내용"),
                        fieldWithPath("data.read").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
                    ),
                ),
            )
    }

    @Test
    fun listNotices() {
        val notices = listOf(
            Notice.fixture(1, "Notice 1", "Content 1", read = true),
            Notice.fixture(2, "Notice 2", "Content 2", read = false),
        )

        every { noticeService.getNotices() } returns notices

        given()
            .get("/api/v1/notices/all")
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
                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("공지사항 제목"),
                        fieldWithPath("data[].content").type(JsonFieldType.STRING).description("공지사항 내용"),
                        fieldWithPath("data[].read").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
                    ),
                ),
            )
    }
}
