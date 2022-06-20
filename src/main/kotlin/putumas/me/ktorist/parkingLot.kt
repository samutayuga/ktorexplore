package putumas.me.ktorist

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.DateFormat

fun Application.parkingLot() {
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")

    log.info("parkinglot from module!")
    routing {
        post("/park") {
            val aVehicle = call.receive<Vehicle>()
            call.application.environment.log.info("receive $aVehicle")
            call.respondText("${aVehicle.type} is parked", status = HttpStatusCode.Created)
        }
    }
}