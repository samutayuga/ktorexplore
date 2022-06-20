package putumas.me.ktorist

import java.time.Instant

enum class Kind {
    CAR, MC, VAN;
}

data class Status(
    val message: String,
    val time: Long
)

data class Vehicle(
    var type: Kind = Kind.CAR,
    val time: Long = Instant.now().toEpochMilli()
)