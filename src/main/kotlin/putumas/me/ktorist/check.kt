package putumas.me.ktorist

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.validate() {
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")
    log.info("Hello from module!")
    routing {
        get("/validate") {
            call.application.environment.log.info("Hello from /api/v1!")
            call.respondText("Listtening on port $portNum")
        }
    }
}