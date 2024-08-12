package com.myongjiway.buslocation

import com.myongjiway.buslocation.BusLocation
import com.myongjiway.buslocation.BusLocationService
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.booleans.shouldBeTrue

class BusLocationTest :
    FeatureSpec({
        lateinit var sut: BusLocationService

        beforeTest {
            sut = BusLocationService()
        }

        feature("좌표 갱신") {
            scenario("좌표 갱신에 성공한다.") {
                // given
                val busLocation = BusLocation("1234", 37.2, 127.2, 151, 1721041381000)

                // when
                val actual = sut.updateBusLocation(busLocation)

                // then
                actual.shouldBeTrue()
            }
        }
    })
