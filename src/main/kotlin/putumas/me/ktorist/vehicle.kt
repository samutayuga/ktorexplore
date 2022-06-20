package putumas.me.ktorist

enum class Kind {
    CAR, MC, VAN;
}

data class Vehicle(var type: Kind = Kind.CAR)