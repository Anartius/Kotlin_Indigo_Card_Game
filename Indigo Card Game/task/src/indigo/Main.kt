package indigo

import kotlin.random.Random

data class DeckCard(val rank: String, val suit: String) {
    fun printDeckCard() {
        print("$rank$suit ")
    }
    fun deckCardAsString(): String {
        return "$rank$suit "
    }

    fun equals(other: DeckCard): Boolean {
        return this.rank == other.rank || this.suit == other.suit
    }
}

data class Player(var scores: Int = 0) {

    val wonCards = mutableListOf<DeckCard>()
    val inHand = mutableListOf<DeckCard>()
}

fun main() {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val deckCards = mutableListOf<DeckCard>()
    var playersTurn: Boolean
    val playerFirst: Boolean
    var playerWonLast: Boolean
    var exit = false
    var lastOnTheTable: String
    val player = Player()
    val computer = Player()
    var choice: Int

    println("Indigo Card Game")
    createDeckCards(ranks, suits, deckCards)
    deckCards.shuffle()

    while (true) {
        println("Play first?")
        when (readLine()!!) {
            "yes" -> {
                playersTurn = true
                playerFirst = true
                playerWonLast = true
                break
            }
            "no" -> {
                playersTurn = false
                playerFirst = false
                playerWonLast = false
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
    println()

    player.inHand.addAll(getDeckCards(deckCards, 6))
    computer.inHand.addAll(getDeckCards(deckCards, 6))

    while (!exit) {

        if (cardsOnTheTable.isNotEmpty()) {
            lastOnTheTable = cardsOnTheTable.last().deckCardAsString()
            println(
                "\n${cardsOnTheTable.size} cards on the table, " +
                        "and the top card is $lastOnTheTable"
            )
        } else println("No cards on the table")

        if (deckCards.isEmpty() &&
            (player.inHand.size == 0 && computer.inHand.size == 0)) {
            exit = true
            break
        }
        if (cardsOnTheTable.size == 52) {
            exit = true
            break
        }

        if (playersTurn) {
            if (player.inHand.size == 0) {
                if (deckCards.isNotEmpty()) {
                    player.inHand.addAll(getDeckCards(deckCards, 6))
                } else {
                    exit = true
                    break
                }
            }
            print("Cards in hand: ")
            for (i in 0 until player.inHand.size) {
                print("${i + 1})${player.inHand[i].deckCardAsString()}")
            }
            println()

            while (true) {
                println("Choose a card to play (1-${player.inHand.size}):")
                val input = readLine()!!
                if (input == "exit") {

                    println("Game Over")
                    return
                }

                try {
                    choice = input.toInt()
                    if (choice !in 1..player.inHand.size) throw NumberFormatException()
                } catch (e: NumberFormatException) {
                    continue
                }

                cardsOnTheTable.add(player.inHand[choice - 1])

                if (cardsOnTheTable.size > 1 &&
                    cardsOnTheTable[cardsOnTheTable.size - 2]
                        .equals(player.inHand[choice - 1])) {

                    playerWonLast = true
                    player.scores += checkScores(cardsOnTheTable)
                    player.wonCards.addAll(cardsOnTheTable)
                    cardsOnTheTable.clear()
                    println("Player wins cards")
                    getScores(player, computer)
                    println()
                }
                player.inHand.removeAt(choice - 1)

                break
            }
            if (computer.inHand.size == 0 && deckCards.size == 0) {
                exit = true
                break
            }
            playersTurn = !playersTurn

        } else {

            while (true) {
                if (computer.inHand.size == 0) {
                    if (deckCards.isNotEmpty()) {
                        computer.inHand.addAll(getDeckCards(deckCards, 6))
                    } else {
                        exit = true
                        break
                    }
                }

                val random = if (computer.inHand.size == 1) {
                    0
                } else Random.nextInt(0, computer.inHand.size - 1)

                cardsOnTheTable.add(computer.inHand[random])
                println("Computer plays ${cardsOnTheTable.last().deckCardAsString()}")

                if (cardsOnTheTable.size > 1 &&
                    cardsOnTheTable[cardsOnTheTable.size - 2]
                        .equals(computer.inHand[random])) {

                    playerWonLast = false
                    computer.scores += checkScores(cardsOnTheTable)
                    computer.wonCards.addAll(cardsOnTheTable)
                    cardsOnTheTable.clear()
                    println("Computer wins cards")
                    getScores(player, computer)
                    println()
                }
                computer.inHand.removeAt(random)
                break
            }
            if (player.inHand.size == 0 && deckCards.size == 0) {
                exit = true
                break
            }
            playersTurn = !playersTurn
        }
    }

    if (exit) {
        if (playerWonLast && cardsOnTheTable.size > 0) {
                player.scores += checkScores(cardsOnTheTable)

            player.wonCards.addAll(cardsOnTheTable)
        } else if (!playerWonLast && cardsOnTheTable.size > 0) {

            computer.scores += checkScores(cardsOnTheTable)
            computer.wonCards.addAll(cardsOnTheTable)
        }

        if (player.scores > computer.scores ||
                player.scores == computer.scores && playerFirst) {

            player.scores += 3
        } else computer.scores += 3

        if (cardsOnTheTable.isNotEmpty()) {
            lastOnTheTable = cardsOnTheTable.last().deckCardAsString()
            println(
                "\n${cardsOnTheTable.size} cards on the table, " +
                        "and the top card is $lastOnTheTable"
            )
        } else println("No cards on the table")

        getScores(player, computer)
        println("Game Over")
        return
    }
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

fun checkScores(cardsOnTheTable: MutableList<DeckCard>): Int {
    val regex = "[A10JQK]".toRegex()
    var i = 0

    cardsOnTheTable.forEach { if (it.rank.contains(regex)) i++ }
    return i
}

fun getScores(player: Player, computer: Player) {
    println("""
        Score: Player ${player.scores} - Computer ${computer.scores}
        Cards: Player ${player.wonCards.size} - Computer ${computer.wonCards.size}
    """.trimIndent())
}