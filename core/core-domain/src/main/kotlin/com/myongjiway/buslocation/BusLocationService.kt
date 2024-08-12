package com.myongjiway.buslocation

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class BusLocationService {

    private val busLocationMap: MutableMap<String, BusLocation> = ConcurrentHashMap()

    fun updateBusLocation(busLocation: BusLocation): Boolean {
        busLocationMap[busLocation.busId] = busLocation
        return true
    }
}
