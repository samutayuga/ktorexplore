package putumas.me.ktorist

import java.time.Instant

enum class Kind {
    CAR, MC, VAN;
}

data class Status(
    val message: String,
    val location: Int = 0,
    val time: Long = 0
)

data class Vehicle(
    var type: Kind = Kind.CAR,
    val time: Long = Instant.now().toEpochMilli()
)