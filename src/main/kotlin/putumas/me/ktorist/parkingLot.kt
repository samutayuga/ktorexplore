package putumas.me.ktorist

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.DateFormat

class LotManager {

    fun checkAvailibility(type: Kind = Kind.VAN): Boolean {
        return false
    }

    fun checkXAvailibility(type: Kind): Boolean {
        return false
    }

    fun park(type: Kind = Kind.VAN): Int {
        return 0
    }

    fun isEmpty(type: Kind = Kind.VAN): Boolean {
        return false
    }

    fun isFull(type: Kind = Kind.VAN): Boolean {
        return false
    }

    fun getAvailableSpots(): Map<Kind, Array<String>> {
        return mapOf()
    }
}


fun Application.parkingLot(lotManager: () -> LotManager) {
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
            val lm = lotManager()

            if (lm.checkAvailibility(aVehicle.type)) {
                val location = lm.park(aVehicle.type)

                // call.respondText("${aVehicle.type} is parked", status = HttpStatusCode.Created)
                call.respond(
                    message = Status(
                        message = "Vehicle ${aVehicle.type} is parked",
                        location = location,
                        time = aVehicle.time
                    ),
                    status = HttpStatusCode.Created
                )
            } else {
                /**
                 * The fact that the vehicle can still be parked to the other designates spots
                 * The system will look further to the other spot
                 * If current vehicle is VAN then system will lookup the spots under
                 * CAR and motorcycle
                 */
                if (lm.checkXAvailibility(aVehicle.type) != null) {
                    val location = lm.park(aVehicle.type)
                    call.respond(
                        message = Status(
                            message = "Vehicle ${aVehicle.type} is parked",
                            location = location,
                            time = aVehicle.time
                        ),
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
}