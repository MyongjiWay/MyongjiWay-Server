package com.myongjiway.core.domain.buslocation

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class BusLocationService {

    private val busLocationMap: MutableMap<String, BusLocation> = ConcurrentHashMap()

    /**
     * 버스의 위치 정보를 업데이트합니다.
     *
     * @param busLocation 업데이트할 버스 위치 정보
     * @return 업데이트 성공 여부 (항상 true를 반환)
     */
    fun updateBusLocation(busLocation: BusLocation): Boolean {
        busLocationMap[busLocation.busId] = busLocation
        return true
    }

    /**
     * 모든 버스의 위치 정보를 반환합니다.
     *
     * @return 모든 버스의 위치 정보 리스트
     */
    fun getBusLocations(): List<com.myongjiway.core.domain.buslocation.BusLocation> {
        return busLocationMap.values.toList()
    }
}
