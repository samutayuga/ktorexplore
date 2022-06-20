package putumas.me.ktorist

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = EngineMain.main(args = args)
fun Application.spot() {
    install(AutoHeadResponse)
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")
    log.info("Hello from spot module!")

    routing {
        get("/spots") {
            call.application.environment.log.info("Hello from /api/v1!")
            call.respondText("Listtening on port $portNum")
        }
    }
}

fun Application.lot() {
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")
    log.info("Hello from lot module!")
    routing {
        get("/lots") {
            call.application.environment.log.info("Hello from /api/v1!")
            call.respondText("Listtening on port $portNum")
        }
    }
}