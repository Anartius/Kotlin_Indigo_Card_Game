class Vehicle {
    inner class Engine {
        fun start() {
            println("RRRrrrrrrr....")
        }
    }
}

fun main() {
    val vehicle = Vehicle()
    vehicle.Engine().start()
}