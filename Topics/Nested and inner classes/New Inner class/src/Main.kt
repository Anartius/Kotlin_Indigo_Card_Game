data class Vehicle(val name: String) {

    inner class Body(val color: String) {
        fun printColor() {
            println("The ${this@Vehicle.name} vehicle has a $color body.")
        }
    }
}
