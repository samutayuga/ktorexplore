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
             * parking lot relies on underlying function to do the
             * job. The routing layer is separated with the business logic that implements
             * the request. So as far as the routing unit test is a concern, we should be able
             * to make the mock for the business logic.
             * The following, parkingLot module accepts the ()->LotManager as an argument
             * LotManager is a class that can be mocked during the unit test, thanks
             * to Mockk
             */
            parkingLot { LotManager() }
        }
        connector {
            port = 8080
        }
    }).start(true)
}
