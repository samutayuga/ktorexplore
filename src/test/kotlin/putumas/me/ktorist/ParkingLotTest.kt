package putumas.me.ktorist

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import io.mockk.*
import java.time.Instant
import kotlin.test.*

enum class TestCase {
    NOLOT,
    LOTAVAILABLE;

}

typealias LotManagerMocker = (TestCase) -> LotManager
typealias LotManagerMockerVerifier = (TestCase, LotManager) -> Unit

val lotManagerMocker: LotManagerMocker = {
    unmockkAll()
    val lotManager = mockk<LotManager>()
    when (it) {
        TestCase.NOLOT -> {
            every { lotManager.checkAvailibility(Kind.VAN) } returns false
            lotManager
        }
        else -> {
            every { lotManager.checkAvailibility(Kind.VAN) } returns true
            every { lotManager.park(Kind.VAN) } returns 0
            lotManager
        }


    }
}
val lotManagerMockerVerifier: LotManagerMockerVerifier = { testCase, lotManager ->
    when (testCase) {
        TestCase.NOLOT -> {
            verify(exactly = 1) { lotManager.checkAvailibility(Kind.VAN) }
        }
        else -> {
            verify(exactly = 1) { lotManager.checkAvailibility(Kind.VAN) }
            verify(exactly = 1) { lotManager.park(Kind.VAN) }
        }


    }
}

class ParkingLotTest {
    var lotManager: LotManager? = null

    @BeforeTest
    fun beforeTest() {


    }

    @AfterTest
    fun afterTest() {

    }

    /**
     * This is nominal test case for parking
     * The parking lot is available
     */
    @Test
    fun testParkingAvailable() = testApplication {
        lotManager = lotManagerMocker(TestCase.LOTAVAILABLE)
        application {
            parkingLot { lotManager!! }
        }
        // LotManager.spotsLoader = spotLoader

        val currentTime: Long = Instant.now().toEpochMilli()
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        client.post("/parking") {
            contentType(ContentType.Application.Json)
            setBody(Vehicle(type = Kind.VAN))
        }.apply {
            val status = this.body<Status>()
            assertEquals(HttpStatusCode.Created, this.status)

            assertEquals(
                "Vehicle VAN is parked",
                status.message,
                message = "Expecting Customer stored correctly got ${status.message}"
            )
            assertTrue(message = "Should be later than $currentTime") { status.time > currentTime }
            assertTrue(message = "Location should be 0 get ${status.location}") { 0 == status.location }
            //verify(exactly = 1) { lotManager!!.checkAvailibility(matchAnyKind()) }
            lotManagerMockerVerifier(TestCase.LOTAVAILABLE, lotManager!!)
        }

    }

    /**
     * This is nominal test case for parking
     * The parking lot is not available
     */
    @Test
    fun testParkingNotAvailable() = testApplication {
        lotManager = lotManagerMocker(TestCase.NOLOT)
        application {
            parkingLot { lotManager!! }
        }
        // LotManager.spotsLoader = spotLoader
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        client.post("/parking") {
            contentType(ContentType.Application.Json)
            setBody(Vehicle(type = Kind.VAN))
        }.apply {
            val status = this.body<Status>()
            assertEquals(HttpStatusCode.NotFound, this.status)

            assertEquals(
                "Cannot find spots for VAN",
                status.message,
                message = "Expecting Customer stored correctly got ${status.message}"
            )
            lotManagerMockerVerifier(TestCase.NOLOT, lotManager!!)
        }

    }
}