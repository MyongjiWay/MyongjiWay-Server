import com.myongjiway.buslocation.BusLocation
import com.myongjiway.buslocation.BusLocationService
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class BusLocationServiceTest : FeatureSpec({
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
            actual shouldBe true
        }
    }

    feature("버스 좌표 리스트 조회") {
        scenario("버스 좌표 리스트를 조회한다.") {
            // given
            val busLocation1 = BusLocation("bus1", 37.5665, 126.9780, 0, 1721041381000)
            val busLocation2 = BusLocation("bus2", 37.5651, 126.9895, 120, 1721041381000)

            sut.updateBusLocation(busLocation1)
            sut.updateBusLocation(busLocation2)

            // when
            val actual = sut.getBusLocations()

            // then
            actual.size shouldBe 2
            actual shouldContainExactlyInAnyOrder listOf(busLocation1, busLocation2)
        }
    }
})
