package com.myongjiway.client.kakao

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class KakaoClientTest :
    FeatureSpec(
        {
            lateinit var sut: KakaoClient
            lateinit var kakaoApi: KakaoApi
            lateinit var restClientConfig: RestClientConfig

            beforeTest {
                kakaoApi = mockk()
                restClientConfig = mockk()
                sut = KakaoClient(kakaoApi, restClientConfig)

                every { restClientConfig.adminKey } returns "adminKey"
            }

            feature("Kakao unlink") {
                scenario("유저의 providerId로 unlink 요청을 보내면 unlinkId를 반환한다.") {
                    // Given
                    every { kakaoApi.unlink(any(), any()) } returns KakaoUnlinkResponse(123123123L)
                    val providerId = 123123123L

                    // When
                    val unlinkId = sut.unlink(providerId.toString())

                    // Then
                    unlinkId.id shouldBe providerId
                }
                scenario("유저의 providerId가 잘못되었으면 400 에러를 반환한다") {
                    // Given
                    val providerId = "123123123"
                    every { kakaoApi.unlink(any(), any()) } throws HttpClientErrorException(HttpStatus.BAD_REQUEST)

                    // When
                    val exception = shouldThrow<HttpClientErrorException> {
                        sut.unlink(providerId)
                    }

                    // Then
                    exception.statusCode shouldBe HttpStatus.BAD_REQUEST
                }
            }
        },
    )
