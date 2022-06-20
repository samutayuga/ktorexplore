package putumas.me.ktorist

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParkingLotTest {
    @BeforeTest
    fun afterTest() {
        unmockkObject(LotManager)
    }

    @Test
    fun testPark() = testApplication {
        mockkObject(LotManager)
        every { LotManager.checkAvailibility(Kind.VAN) } returns true

        val currentTime: Long = Instant.now().toEpochMilli()
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val resp = client.post("/parking") {
            contentType(ContentType.Application.Json)
            setBody(Vehicle(type = Kind.VAN))
        }
        val status = resp.body<Status>()
        assertEquals(HttpStatusCode.Created, resp.status)

        assertEquals(
            "Vehicle VAN is parked",
            status.message,
            message = "Expecting Customer stored correctly got ${status.message}"
        )
        assertTrue(message = "Should be later than $currentTime") { status.time > currentTime }
        verify(exactly = 1) { LotManager.checkAvailibility(Kind.VAN) }
    }
}