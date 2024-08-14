package com.myongjiway.core.api.controller.v1.response

import com.myongjiway.core.domain.buslocation.BusLocation

data class GetBusLocationRes(
    val busId: String,
    val latitude: Double,
    val longitude: Double,
    val direction: Int,
) {
    constructor(busLocation: BusLocation) : this(
        busId = busLocation.busId,
        latitude = busLocation.latitude,
        longitude = busLocation.longitude,
        direction = busLocation.direction,
    )
}
