package indigo

import kotlin.random.Random

data class DeckCard(val rank: String, val suit: String) {
    fun printDeckCard() {
        print("$rank$suit ")
    }
    fun deckCardAsString(): String {
        return "$rank$suit "
    }
}

fun main() {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val deckCards = mutableListOf<DeckCard>()
    var playersTurn: Boolean
    var lastOnTheTable: String
    var playerCards: MutableList<DeckCard>
    var computerCards: MutableList<DeckCard>
    var choice: Int

    println("Indigo Card Game")
    createDeckCards(ranks, suits, deckCards)
    deckCards.shuffle()

    while (true) {
        println("Play first?")
        when (readLine()!!) {
            "yes" -> {
                playersTurn = true
                break
            }
            "no" -> {
                playersTurn = false
                break
            }
            "exit" -> {
                println("Game Over")
                return
            }
            else -> continue
        }
    }

    print("Initial cards on the table: ")
    val cardsOnTheTable = getDeckCards(deckCards, 4)
    cardsOnTheTable.forEach { it.printDeckCard() }
    println("\n")

    playerCards = getDeckCards(deckCards, 6)
    computerCards = getDeckCards(deckCards, 6)

    while (true) {
        lastOnTheTable = cardsOnTheTable.last().deckCardAsString()
        println("${cardsOnTheTable.size} cards on the table, " +
            "and the top card is $lastOnTheTable"
        )
        if (cardsOnTheTable.size == 52) break

        if (playersTurn) {
            if (playerCards.size == 0) {
                playerCards = getDeckCards(deckCards, 6)
            }
            print("Cards in hand: ")
            for (i in 0 until playerCards.size) {
                print("${i + 1})${playerCards[i].deckCardAsString()}")
            }
            println()

            while (true) {
                println("Choose a card to play (1-${playerCards.size}):")
                val input = readLine()!!
                if (input == "exit") {
                    println("Game Over")
                    return
                }
                try {
                    choice = input.toInt()
                    if (choice !in 1..playerCards.size) throw NumberFormatException()
                } catch (e: NumberFormatException) {
                    continue
                }
                cardsOnTheTable.add(playerCards[choice - 1])
                playerCards.removeAt(choice - 1)
                break
            }
            playersTurn = !playersTurn

        } else {

            while (true) {
                if (computerCards.size == 0) {
                    computerCards = getDeckCards(deckCards, 6)
                }

                val random = if (computerCards.size == 1) {
                    0
                } else Random.nextInt(0, computerCards.size - 1)

                cardsOnTheTable.add(computerCards[random])
                computerCards.removeAt(random)
                println("Computer plays ${cardsOnTheTable.last().deckCardAsString()}\n")
                break
            }
            playersTurn = !playersTurn
        }
    }
    println("Game Over")
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

fun getDeckCards(deckCards: MutableList<DeckCard>,
                numberOfCards: Int): MutableList<DeckCard> {
    val output = mutableListOf<DeckCard>()

    for (i in 1..numberOfCards) {
        output.add(deckCards.first())
        deckCards.removeFirst()
    }
    return output
}