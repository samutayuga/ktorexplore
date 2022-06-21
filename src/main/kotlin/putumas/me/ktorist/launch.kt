package putumas.me.ktorist

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

/**
 * There is an issue with testability when implementing the launcher with Enginemain.
 * It is not possible to mock the underlying class or object
 * for the server internally.
 * For example when testing the route, where the route will use one or many layer to implement
 * business logic.
 * As long as the routing testing is a concern
 * we do not need to have the actual business logic
 * It can be a mock. That is the purpose of unit test
 */
fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        config = HoconApplicationConfig(ConfigFactory.load())
        module {

            /**
             * partking lot relies on underlying function to do the
             * job. An that is to be a mock for the unit test
             * So, we need to be able to make the
             */
            parkingLot { LotManager() }
        }
        connector {
            port = 8080
        }
    }).start(true)
}
