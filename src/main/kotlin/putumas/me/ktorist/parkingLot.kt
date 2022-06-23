package putumas.me.ktorist

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.DateFormat

/**
 * A business logic layer to serve the routing
 */
class LotManager {
    /**
     * Check if the spots for a given vehicle Kind is available
     * @param type,is a Kind, which is an enumeration of different vehicle type.
     *              currently it supports CAR, VAN and MO, (motorcycle)
     * @return a boolean true means available and false means not available
     */
    fun checkAvailibility(type: Kind = Kind.VAN): Boolean {
        return false
    }

    /**
     * There is a situation where for a given spot return by checkAvailibility return false
     * But system understand that it can park on the others spots.
     * As long as it is available, considering the size, accordingly, for example, 1 van spot
     * is equivalent to 2 car spots. Then this function will return a map object that has a kind as key and a string
     * representing the spots layout
     * @param type,is a Kind, which is an enumeration of different vehicle type.
     *              currently it supports CAR, VAN and MO, (motorcycle)
     * @return a map, Kind by String, eg, CAR: ooooooxxxxxxx , x means unavailable, o means available
     */
    fun getXAvailableSpots(type: Kind): Map<Kind, String> {
        return mapOf()
    }

    /**
     * Claim the spots for a given type
     * @param type,is a Kind, which is an enumeration of different vehicle type.
     *              currently it supports CAR, VAN and MO, (motorcycle)
     * @return index location of the spot taken
     */
    fun park(type: Kind = Kind.VAN): Int {
        return 0
    }

    /**
     * Check if a spot for a given type is not available
     * @param type,is a Kind, which is an enumeration of different vehicle type.
     *              currently it supports CAR, VAN and MO, (motorcycle)
     * @return a boolean true means empty and false means at least 1 slot is available
     */
    fun isEmpty(type: Kind = Kind.VAN): Boolean {
        return false
    }

    /**
     * Check if a spot for a given type is available
     * @param type,is a Kind, which is an enumeration of different vehicle type.
     *              currently it supports CAR, VAN and MO, (motorcycle)
     * @return a boolean true means full and false means not full yet
     */
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
                if (lm.getXAvailableSpots(aVehicle.type) != null) {
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