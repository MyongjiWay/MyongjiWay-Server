package com.myongjiway.core.api.controller.v1

import com.myongjiway.client.kakao.KakaoClient
import com.myongjiway.client.kakao.model.KakaoUnlinkResult
import com.myongjiway.core.api.controller.UserController
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
import com.myongjiway.user.UserService
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

class UserControllerTest : RestDocsTest() {

    private lateinit var userService: UserService
    private lateinit var kakaoClient: KakaoClient
    private lateinit var controller: UserController

    @BeforeEach
    fun setUp() {
        userService = mockk()
        kakaoClient = mockk()
        controller = UserController(userService, kakaoClient)
        mockMvc = mockController(controller)
    }

    @Test
    fun inactive() {
        every { kakaoClient.unlink(any()) } returns KakaoUnlinkResult(123123123)
        every { userService.inactive(any()) } returns 1000L

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer token")
            .patch("/api/v1/users/inactive")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "userInactive",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
                        fieldWithPath("data.userId").type(JsonFieldType.STRING).description("Inactive User Id"),
                        fieldWithPath("error").type(JsonFieldType.NULL).ignored(),
                    ),
                ),
            )
    }
}
