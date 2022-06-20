package putumas.me.ktorist

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = EngineMain.main(args = args)
fun Application.greeting() {
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")
    log.info("Hello from module!")
    routing {
        get("/greeting") {
            call.application.environment.log.info("Hello from /api/v1!")
            call.respondText("Listtening on port $portNum")
        }
    }
}

fun Application.serious() {
    val portNum = environment.config.propertyOrNull("ktor.deployment.port")
    log.info("Hello from module!")
    routing {
        get("/serious") {
            call.application.environment.log.info("Hello from /api/v1!")
            call.respondText("Listtening on port $portNum")
        }
    }
}