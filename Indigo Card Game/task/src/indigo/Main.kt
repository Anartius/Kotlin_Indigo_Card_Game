package indigo

data class DeckCard(val rank: String, val suit: String) {
    fun printDeckCard() {
        print("$rank$suit ")
    }
}

fun main() {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val deckCards = mutableListOf<DeckCard>()
    createDeckCards(ranks, suits, deckCards)

    while(true) {
        println("Choose an action (reset, shuffle, get, exit):")
        when (readLine()!!) {
            "reset" -> {
                createDeckCards(ranks, suits, deckCards)
                println("Card deck is reset.")
            }
            "shuffle" -> {
                deckCards.shuffle()
                println("Card deck is shuffled.")
            }
            "get" -> getDeckCard(deckCards)
            "exit" -> {
                print("Bye")
                break
            }
            else -> println("Wrong action.")
        }
    }
//    deckCards.forEach { print("${it.rank}${it.suit} ") }
}

fun createDeckCards(ranks: List<String>, suits: List<String>,
                    deckCards: MutableList<DeckCard>) {
    deckCards.clear()
    for (i in suits.indices) {
        for (j in ranks.indices) {
            deckCards.add(DeckCard(ranks[j], suits[i]))
        }
    }
}

fun getDeckCard(deckCards: MutableList<DeckCard>) {
    val numberOfCards: Int
    println("Number of cards:")

    try {
        numberOfCards = readLine()!!.toInt()
        if (numberOfCards !in 1..52) throw NumberFormatException()
    } catch (e: NumberFormatException) {
        println("Invalid number of cards.")
        return
    }

    if (numberOfCards > deckCards.size) {
        println("The remaining cards are insufficient to meet the request.")
        return
    }

    for (i in 1..numberOfCards) {
        deckCards.first().printDeckCard()
        deckCards.removeFirst()
    }
    println()
}