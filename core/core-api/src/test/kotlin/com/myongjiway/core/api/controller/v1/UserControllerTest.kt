package com.myongjiway.core.api.controller.v1

import com.myongjiway.client.kakao.KakaoClient
import com.myongjiway.client.kakao.model.KakaoUnlinkResult
import com.myongjiway.core.api.controller.UserController
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
import com.myongjiway.token.TokenService
import com.myongjiway.user.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

class UserControllerTest : RestDocsTest() {

    private lateinit var tokenService: TokenService
    private lateinit var userService: UserService
    private lateinit var kakaoClient: KakaoClient
    private lateinit var controller: UserController

    @BeforeEach
    fun setUp() {
        userService = mockk()
        kakaoClient = mockk()
        controller = UserController(userService, tokenService, kakaoClient)
        mockMvc = mockController(controller)
    }

    @Test
    fun inactive() {
        every { kakaoClient.unlink(any()) } returns KakaoUnlinkResult(123123123)
        every { userService.inactive(any()) } returns 1000L

        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
            .patch("/api/v1/users/inactive")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "inactiveUser",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("result").description("Result"),
                        fieldWithPath("data.userId").description("Inactive User Id"),
                        fieldWithPath("error").ignored(),
                    ),
                ),
            )
    }
}
