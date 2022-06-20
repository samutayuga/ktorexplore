package putumas.me.ktorist

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParkingLotTest {
    @Test
    fun testPark() = testApplication {
        val currentTime: Long = Instant.now().toEpochMilli()
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val resp = client.post("/park") {
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

    }
}