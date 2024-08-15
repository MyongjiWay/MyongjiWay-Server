package com.myongjiway.core.api.controller

import com.myongjiway.core.api.controller.v1.response.GetBusLocationRes
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.buslocation.BusLocationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/bus-locations")
@RestController
class BusLocationController(
    private var busLocationService: BusLocationService,
) {
    @GetMapping
    fun getBusLocation(): ApiResponse<List<GetBusLocationRes>> {
        val res = busLocationService.getBusLocations()
        return ApiResponse.success(res.map { GetBusLocationRes(it) })
    }
}
