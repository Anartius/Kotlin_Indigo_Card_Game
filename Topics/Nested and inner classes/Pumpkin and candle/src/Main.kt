class Pumpkin(val type: String, val isForHalloween: Boolean) {

    fun addCandle() {
        if (isForHalloween) {
            val pumpkin = Pumpkin(this.type, this.isForHalloween)
            val candle = pumpkin.Candle()
            candle.burning()
        } else println("We don't need a candle.")
    }

    inner class Candle {
        fun burning() {
            println("The candle is burning inside this spooky $type pumpkin! Boooooo!")
        }
    }
}