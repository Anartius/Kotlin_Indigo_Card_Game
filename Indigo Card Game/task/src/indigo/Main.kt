package indigo

import kotlin.random.Random

class DeckCard(val rank: String, val suit: String) {
    fun printDeckCard() {
        print("$rank$suit ")
    }
    fun deckCardAsString(): String {
        return "$rank$suit "
    }

    fun equalCard(other: DeckCard): Boolean {
        return this.rank == other.rank || this.suit == other.suit
    }
}

class Player(var scores: Int = 0) {

    val wonCards = mutableListOf<DeckCard>()
    val inHand = mutableListOf<DeckCard>()

    fun printCardsInHand() {
        for (i in 0 until inHand.size) {
            print("${i + 1})${inHand[i].deckCardAsString()}")
        }
        println()
    }
}

class Cards (private val ranks: List<String>, private val suits: List<String>) {

    val deckCards = mutableListOf<DeckCard>()
    val cardsOnTheTable = mutableListOf<DeckCard>()

    fun createDeckCards() {
        deckCards.clear()
        for (i in suits.indices) {
            for (j in ranks.indices) {
                deckCards.add(DeckCard(ranks[j], suits[i]))
            }
        }
        deckCards.shuffle()
    }

    fun getDeckCards(target: MutableList<DeckCard>, amount: Int) {

        for (i in 1..amount) {
            target.add(deckCards.first())
            deckCards.removeFirst()
        }
    }

    fun printTable() {

        if (cardsOnTheTable.isNotEmpty()) {
            val lastOnTheTable = cardsOnTheTable.last().deckCardAsString()
            println(
                "\n${cardsOnTheTable.size} cards on the table, " +
                        "and the top card is $lastOnTheTable"
            )
        } else println("No cards on the table")
    }

    fun checkScores(): Int {
        val regex = "[A10JQK]".toRegex()
        var i = 0

        cardsOnTheTable.forEach { if (it.rank.contains(regex)) i++ }
        return i
    }

    fun moveTableTo(player: Player) {

        player.wonCards.addAll(cardsOnTheTable)
        cardsOnTheTable.clear()
    }
}

fun main() {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val player = Player()
    val computer = Player()
    val cards = Cards(ranks, suits)
    val cardsOnTheTable = cards.cardsOnTheTable
    var playersTurn: Boolean
    val playerFirst: Boolean
    var playerWonLast: Boolean
    var exit = false
    var choice: Int

    println("Indigo Card Game")
    cards.createDeckCards()

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
    cards.getDeckCards(cardsOnTheTable, 4)
    cardsOnTheTable.forEach { it.printDeckCard() }
    println()

    cards.getDeckCards(player.inHand, 6)
    cards.getDeckCards(computer.inHand, 6)

    while (!exit) {

        cards.printTable()

        if (cards.deckCards.isEmpty() &&
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
                if (cards.deckCards.isNotEmpty()) {
                    cards.getDeckCards(player.inHand, 6)
                } else {
                    exit = true
                    break
                }
            }
            print("Cards in hand: ")
            player.printCardsInHand()

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
                        .equalCard(player.inHand[choice - 1])) {

                    playerWonLast = true
                    player.scores += cards.checkScores()
                    cards.moveTableTo(player)
                    println("Player wins cards")
                    getScores(player, computer)
                    println()
                }
                player.inHand.removeAt(choice - 1)

                break
            }

            if (computer.inHand.size == 0 && cards.deckCards.size == 0) {
                exit = true
                break
            }
            playersTurn = !playersTurn

        } else {

            var computerChoice: DeckCard
            while (true) {
                if (computer.inHand.size == 0) {
                    if (cards.deckCards.isNotEmpty()) {
                        cards.getDeckCards(computer.inHand,6)
                    } else {
                        exit = true
                        break
                    }
                }
                for (i in 0 until computer.inHand.size) {
                    print(computer.inHand[i].deckCardAsString())
                }
                println()

                computerChoice = if (computer.inHand.size == 1) {
                    computer.inHand[0]
                } else getCandidate(computer.inHand, cardsOnTheTable)

                cardsOnTheTable.add(computerChoice)
                println("Computer plays " +
                        cardsOnTheTable.last().deckCardAsString()
                )

                if (cardsOnTheTable.size > 1 &&
                    cardsOnTheTable[cardsOnTheTable.size - 2]
                        .equalCard(computerChoice)) {

                    playerWonLast = false
                    computer.scores += cards.checkScores()
                    cards.moveTableTo(computer)
                    println("Computer wins cards")
                    getScores(player, computer)
                    println()
                }
                computer.inHand.remove(computerChoice)
                break
            }
            if (player.inHand.size == 0 && cards.deckCards.size == 0) {
                exit = true
                break
            }
            playersTurn = !playersTurn
        }
    }

    if (exit) {

        cards.printTable()

        if (playerWonLast && cardsOnTheTable.size > 0) {

            player.scores += cards.checkScores()
            cards.moveTableTo(player)

        } else if (!playerWonLast && cardsOnTheTable.size > 0) {

            computer.scores += cards.checkScores()
            cards.moveTableTo(computer)
        }

        if (player.scores > computer.scores ||
            (player.scores == computer.scores) && playerFirst) {

            player.scores += 3
        } else computer.scores += 3

        getScores(player, computer)
        println("Game Over")
        return
    }
}

fun getScores(player: Player, computer: Player) {
    println("""
        Score: Player ${player.scores} - Computer ${computer.scores}
        Cards: Player ${player.wonCards.size} - Computer ${computer.wonCards.size}
    """.trimIndent())
}

fun getCandidate(cardsInHand: MutableList<DeckCard>,
                 onTheTable: MutableList<DeckCard>) : DeckCard {
    val random = Random
    val inHand = mutableListOf<DeckCard>()
    inHand.addAll(cardsInHand)
    val inHandTemp = mutableListOf<DeckCard>()
    val candidateCards = mutableListOf<DeckCard>()
    val filtered = mutableListOf<DeckCard>()

    try {
        for (i in cardsInHand.indices) {
            if (cardsInHand[i].equalCard(onTheTable.last())) candidateCards.add(inHand[i])
        }
    } catch (e: NoSuchElementException) {
        candidateCards.clear()
    }
    if (candidateCards.size == 1) return candidateCards[0]

    if (candidateCards.size > 1) {
        if (candidateCards.size == 3) {
            inHand.clear()
            inHand.addAll(candidateCards)

            for (i in inHand.indices) {
                var k = 0
                for (j in inHand.indices) {
                    if (inHand[i].equalCard(inHand[j])) k++
                }
                if (k == 1) candidateCards.removeAt(i)
            }
        }
        return candidateCards[random.nextInt(0, candidateCards.size - 1)]
    }

    while (inHand.size > 0){
        inHandTemp.addAll(inHand.filter { it.suit == inHand.first().suit })
        if (inHandTemp.size > 1) candidateCards.addAll(inHandTemp)
        filtered.addAll(inHand.filter { it.suit != inHandTemp.first().suit })
        inHand.clear()
        inHand.addAll(filtered)
        filtered.clear()
        inHandTemp.clear()
    }

    inHand.clear()
    if (candidateCards.size > 1) {
        inHand.addAll(candidateCards)
    } else inHand.addAll(cardsInHand)

    if (candidateCards.isEmpty()) {
        while (inHand.size > 0){
            inHandTemp.addAll(inHand.filter { it.rank == inHand.first().rank })
            if (inHandTemp.size > 1) candidateCards.addAll(inHandTemp)
            filtered.addAll(inHand.filter { it.rank != inHandTemp.first().rank })
            inHand.clear()
            inHand.addAll(filtered)
            filtered.clear()
            inHandTemp.clear()
        }
    }

    return if (inHand.size > 1) {
        inHand[random.nextInt(0, inHand.size - 1)]
    } else cardsInHand[random.nextInt(0, cardsInHand.size - 1)]
}