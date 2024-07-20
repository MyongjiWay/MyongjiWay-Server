package com.myongjiway.core.auth.controller

import com.example.util.com.myongjiway.MockMvcExtensions.andReturnAs
import com.example.util.com.myongjiway.MockMvcExtensions.expectStatus
import com.example.util.com.myongjiway.MockMvcExtensions.post
import com.myongjiway.core.api.controller.ApiControllerAdvice
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.auth.security.domain.AuthService
import com.myongjiway.core.auth.security.domain.KakaoLoginData
import com.myongjiway.core.auth.security.domain.TokenResult
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AuthControllerTest :
    FeatureSpec(
        {
            lateinit var mockMvc: MockMvc
            lateinit var authService: AuthService
            lateinit var sut: AuthController

            beforeTest {
                authService = mockk(relaxed = true)
                sut = AuthController(authService)
                mockMvc = MockMvcBuilders.standaloneSetup(sut)
                    .setControllerAdvice(ApiControllerAdvice())
                    .build()
            }

            feature("카카오 로그인") {
                scenario("카카오 로그인에 성공한다") {
                    // given
                    every { authService.kakaoLogin(any()) }.returns(
                        TokenResult(
                            accessToken = "accesstoken",
                            refreshToken = "refreshtoken",
                        ),
                    )

                    // when
                    val actual = mockMvc.post(
                        "/auth/kakao-login",
                        KakaoLoginData(
                            providerId = "123123123",
                            username = "test",
                            profileImg = "test.img",
                        ),
                        HttpHeaders(),
                        contentType = MediaType.APPLICATION_JSON,
                    ).expectStatus(HttpStatus.OK)
                        .andDo { result ->
                            println("Response: ${result.response.contentAsString}")
                        }
                        .andReturnAs<ApiResponse<TokenResult>>()

                    // then
                    val expect = ApiResponse.success(
                        TokenResult(
                            "accesstoken",
                            "refreshtoken",
                        ),
                    )

                    actual shouldBe expect
                }
            }
        },
    )
