package com.myongjiway.core.api.controller.v1.notice

import com.myongjiway.core.api.controller.NoticeAdminController
import com.myongjiway.core.api.controller.v1.request.NoticeRequest
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters

class NoticeAdminControllerDocsTest : RestDocsTest() {
    private lateinit var noticeService: NoticeService
    private lateinit var noticeAdminController: NoticeAdminController

    @BeforeEach
    fun setUp() {
        noticeService = mockk()
        noticeAdminController = NoticeAdminController(noticeService)
        mockMvc = mockController(noticeAdminController)
    }

    @Test
    fun createNotice() {
        val noticeRequest = NoticeRequest(title = "New Notice", author = "장호진", content = "Notice Content")

        every { noticeService.createNotice(any()) } just Runs

        given()
            .contentType(ContentType.JSON)
            .body(noticeRequest)
            .post("/admin/api/v1/notices")
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
                        fieldWithPath("author").type(JsonFieldType.STRING).description("이건 안넣어도 됩니다."),
                    ),
                ),
            )
    }

    @Test
    fun updateNotice() {
        val noticeId = 1L
        val noticeRequest = NoticeRequest("Updated Notice", author = "장호진", "Updated Content")

        every { noticeService.updateNotice(any(), noticeId) } just Runs

        given()
            .contentType(ContentType.JSON)
            .body(noticeRequest)
            .patch("/admin/api/v1/notices/{noticeId}", noticeId.toString())
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
                        fieldWithPath("author").type(JsonFieldType.STRING).description("이건 안넣어도 됩니다."),
                    ),
                ),
            )
    }

    @Test
    fun deleteNotice() {
        val noticeId = 1L

        every { noticeService.deleteNotice(any()) } just Runs

        given()
            .delete("/admin/api/v1/notices/{noticeId}", noticeId.toString())
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
}
