package com.myongjiway.core.auth.controller

import com.myongjiway.core.api.controller.v1.request.RefreshRequest
import com.myongjiway.core.auth.controller.v1.request.KakaoLoginRequest
import com.myongjiway.core.auth.security.domain.AuthService
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils.requestPreprocessor
import com.myongjiway.test.api.RestDocsUtils.responsePreprocessor
import com.myongjiway.token.TokenResult
import com.myongjiway.token.TokenService
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

class AuthControllerTest : RestDocsTest() {

    private lateinit var authService: AuthService
    private lateinit var tokenService: TokenService
    private lateinit var controller: AuthController

    @BeforeEach
    fun setUp() {
        authService = mockk()
        tokenService = mockk()
        controller = AuthController(authService, tokenService)
        mockMvc = mockController(controller)
    }

    @Test
    fun kakaoLoginPost() {
        every { authService.kakaoLogin(any()) } returns TokenResult("ACCESS_TOKEN", "REFRESH_TOKEN")

        given()
            .contentType(ContentType.JSON)
            .body(KakaoLoginRequest("123123123", "박세진", "https://profile.com"))
            .post("/auth/kakao-login")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "kakaoLoginPost",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("providerId").type(JsonFieldType.STRING).description("123123123"),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("박세진"),
                        fieldWithPath("profileImg").type(JsonFieldType.STRING).description("https://profile.com"),
                    ),
                    responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("ACCESS_TOKEN"),
                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("REFRESH_TOKEN"),
                        fieldWithPath("error").type(JsonFieldType.STRING).ignored(),
                    ),
                ),
            )
    }

    @Test
    fun refresh() {
        every { tokenService.refresh(any()) } returns TokenResult("ACCESS_TOKEN", "REFRESH_TOKEN")

        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
            .contentType(ContentType.JSON)
            .body(RefreshRequest("refreshToken"))
            .post("/auth/refresh")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "refreshToken",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh Token"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("Result"),
                        fieldWithPath("data.accessToken").description("Access Token"),
                        fieldWithPath("data.refreshToken").description("Refresh Token"),
                        fieldWithPath("error").ignored(),
                    ),
                ),
            )
    }
}
