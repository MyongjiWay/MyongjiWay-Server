package com.myongjiway.core.api.controller.v1.buslocation

import com.myongjiway.core.api.controller.BusLocationController
import com.myongjiway.core.domain.buslocation.BusLocation
import com.myongjiway.core.domain.buslocation.BusLocationService
import com.myongjiway.test.api.RestDocsTest
import com.myongjiway.test.api.RestDocsUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation

class BusLocationControllerTest : RestDocsTest() {

    private lateinit var sut: BusLocationController
    private lateinit var busLocationService: BusLocationService

    @BeforeEach
    fun setUp() {
        busLocationService = mockk()
        sut = BusLocationController(busLocationService)
        mockMvc = mockController(sut)
    }

    @Test
    fun getBusLocation() {
        val busLocations = listOf(
            BusLocation("bus1", 37.5665, 126.9780, 0, 1721041381000),
            BusLocation("bus2", 37.5651, 126.9895, 120, 1721041381000),
        )
        every { busLocationService.getBusLocations() } returns busLocations

        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
            .get("/api/v1/bus-locations")
            .then()
            .status(HttpStatus.OK)
            .apply(
                MockMvcRestDocumentation.document(
                    "busLocation/get",
                    RestDocsUtils.responsePreprocessor(),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.STRING)
                            .description("ResultType"),
                        PayloadDocumentation.fieldWithPath("data[].busId").type(JsonFieldType.STRING)
                            .description("버스 ID"),
                        PayloadDocumentation.fieldWithPath("data[].latitude").type(JsonFieldType.NUMBER)
                            .description("위도"),
                        PayloadDocumentation.fieldWithPath("data[].longitude").type(JsonFieldType.NUMBER)
                            .description("경도"),
                        PayloadDocumentation.fieldWithPath("data[].direction").type(JsonFieldType.NUMBER)
                            .description("방향"),
                        PayloadDocumentation.fieldWithPath("error").type(JsonFieldType.STRING).ignored(),
                    ),
                ),
            )
    }
}
