package putumas.me.ktorist

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ParkingLotTest {
    @Test
    fun testPark() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val resp = client.post("/park") {
            contentType(ContentType.Application.Json)
            setBody(Vehicle(type = Kind.VAN))
        }
        val text = resp.bodyAsText()
        assertEquals(HttpStatusCode.Created, resp.status)
        assertEquals(
            "VAN is parked",
            resp.bodyAsText(),
            message = "Expecting Customer stored correctly got $text"
        )

    }
}