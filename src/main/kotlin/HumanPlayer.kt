import java.util.InputMismatchException
import java.util.Scanner
import kotlin.IllegalArgumentException
import kotlin.system.exitProcess

class HumanPlayer(playerNumber: Int, deck: Deck, pile: Deck) : Player(playerNumber, deck, pile) {
    // There is a protected Deck object called 'hand'

    private fun validateResponse(response: String) : String { // True if valid, false if invalid
        val temp = response.trim()
        val words = temp.split("\\s+".toRegex())

        if (words.size != 2) { // Response must be 2 words only.
            return "Bad response"
        }

        var output : String = when (words[0].lowercase()) {
            "a", "ace" -> "A"
            "j", "jack" -> "J"
            "q", "queen" -> "Q"
            "k", "king" -> "K"
            "2", "two" -> "2"
            "3", "three" -> "3"
            "4", "four" -> "4"
            "5", "five" -> "5"
            "6", "six" -> "6"
            "7", "seven" -> "7"
            "8", "eight" -> "8"
            "9", "nine" -> "9"
            "10", "ten" -> "T"
            else -> return "Bad response"
        }

        output += when (words[1].lowercase()) {
            "spade", "spades" -> "S"
            "diamond", "diamonds" -> "D"
            "heart", "hearts" -> "H"
            "club", "clubs" -> "C"
            else -> return "Bad response"
        }

        var cardFound = false

        for (card in hand) { // Final check to ensure that said card is actually in player's hand
            if (output == card.cardCode()) {
                cardFound = true
                break
            }
        }

        if (!cardFound) { output = "Bad response" }

        return output
    }

    fun playRound(latestEight: Boolean = false, opponentChosenSuit: String = "") : String {
        val latest : Card = if (latestEight) {
            Card(opponentChosenSuit, '8') // Temporary card in case latest is an 8
        } else {
            pile.topOfDeck()
        }

        chosenSuit = ""
        val scanner = Scanner(System.`in`)
        var cardsDrawn = 0

        println("-----".repeat(20))
        println("It is now your turn.")
        println("Card on pile: $latest")
        println("\nYour hand: $hand")

        while (!checkHand(latest)) {
            println("\nYou do not have a playable card, drawing from deck...")
            Thread.sleep(650)
            if (!deck.isDeckEmpty()) {
                drawOneCard()
            } else {
                println("Deck is empty. Reshuffling...")
                reshufflePileToDeck()
                Thread.sleep(300)
                println("New card taken")
                drawOneCard()
            }
            cardsDrawn++
            println("New hand: $hand")
        }

        if (cardsDrawn > 9) {
            println("Ooooooh... $cardsDrawn drawn cards. That's rough...")
        }

        println("\nYou have a playable card!")

        println("Which card will you play?")

        lateinit var cardPlayed : String

        while (true) {
            try {
                cardPlayed = scanner.nextLine()
                if (cardPlayed == "quit") {
                    println("Goodbye!")
                    exitProcess(0)
                }

                val validation = validateResponse(cardPlayed)

                if (validation == "Bad response")
                    throw IllegalArgumentException()

                cardPlayed = validation
                break // If the code reaches here, the response was valid
            } catch (e: InputMismatchException) {
                println("Invalid response. To play a card, type the value as the first word and the suit as the second.")
            } catch (e: IllegalArgumentException) {
                println("Invalid response. To play a card, type the value as the first word and the suit as the second.")
            }
        }

        for (card in hand) {
            if (card.cardCode() == cardPlayed) {
                if (card.cardValue == '8') {
                    println("You played a crazy eight! What should the next suit be?")

                    while (true) {
                        val choice = scanner.nextLine()

                        if (choice == "quit") {
                            println("Goodbye!")
                            exitProcess(0)
                        }

                        chosenSuit = when (choice.lowercase()) {
                            "d", "diamond", "diamonds" -> "Diamonds"
                            "h", "heart", "hearts" -> "Hearts"
                            "c", "club", "clubs" -> "Clubs"
                            "s", "spade", "spades" -> "Spades"
                            else -> "Invalid response"
                        }

                        if (chosenSuit == "Invalid response") { // Response validation
                            println("Please type a suit")
                            continue
                        }

                        break // If the code reaches here, the input was successful
                    }
                }

                handToPile(card) // Code reaches here if anything other than an 8 was played
                break
            }
        }

        println("\nYour new hand: $hand")

        return chosenSuit
    }
}