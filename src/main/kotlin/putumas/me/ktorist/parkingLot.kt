package putumas.me.ktorist

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.DateFormat

object LotManager {

    init {

    }

    fun checkAvailibility(kind: Kind): Boolean {
        return false
    }
}

fun Application.parkingLot() {
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    log.info("parkinglot from module!")
    routing {
        post("/parking") {
            val aVehicle = call.receive<Vehicle>()
            call.application.environment.log.info("receive $aVehicle")
            if (LotManager.checkAvailibility(aVehicle.type)) {

                // call.respondText("${aVehicle.type} is parked", status = HttpStatusCode.Created)
                call.respond(
                    message = Status("Vehicle ${aVehicle.type} is parked", time = aVehicle.time),
                    status = HttpStatusCode.Created
                )
            } else {
                call.respond(
                    message = Status("Cannot find spots for ${aVehicle.type}", time = 0),
                    status = HttpStatusCode.NotFound
                )
            }

        }
    }
}