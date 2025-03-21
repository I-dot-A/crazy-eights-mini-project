import java.util.InputMismatchException
import java.util.Scanner
import kotlin.IllegalArgumentException
import kotlin.math.abs
import kotlin.system.exitProcess

class CrazyEights {
    private val deck = Deck(true)
    private val pile = Deck(true)
    private lateinit var playerOne : HumanPlayer
    private lateinit var playerTwo : ComputerPlayer
    private var latestSuitIfEight : String = ""
    private var playerOneScore = 0
    private var playerTwoScore = 0

    init { // Man, I love Kotlin
        waitForPlayerConfirmation()
    }

    private fun sillyTitleDrop() { // Self-explanatory
        println("\n" +
            "░█████╗░██████╗░░█████╗░███████╗██╗░░░██╗  ███████╗██╗░██████╗░██╗░░██╗████████╗░██████╗\n" +
            "██╔══██╗██╔══██╗██╔══██╗╚════██║╚██╗░██╔╝  ██╔════╝██║██╔════╝░██║░░██║╚══██╔══╝██╔════╝\n" +
            "██║░░╚═╝██████╔╝███████║░░███╔═╝░╚████╔╝░  █████╗░░██║██║░░██╗░███████║░░░██║░░░╚█████╗░\n" +
            "██║░░██╗██╔══██╗██╔══██║██╔══╝░░░░╚██╔╝░░  ██╔══╝░░██║██║░░╚██╗██╔══██║░░░██║░░░░╚═══██╗\n" +
            "╚█████╔╝██║░░██║██║░░██║███████╗░░░██║░░░  ███████╗██║╚██████╔╝██║░░██║░░░██║░░░██████╔╝\n" +
            "░╚════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░  ╚══════╝╚═╝░╚═════╝░╚═╝░░╚═╝░░░╚═╝░░░╚═════╝░")
    }

    private fun waitForPlayerConfirmation() { // When the program starts, the player needs to type 'play'
        sillyTitleDrop()
        val scanner = Scanner(System.`in`)
        println("Type 'play' to start.")

        var input = scanner.nextLine()
        var iterations = 0
        while (input != "play") { // Will keep looping until the player puts in the correct input
            if (input == "quit") {
                println("Goodbye!")
                exitProcess(0)
            }

            if (iterations != 0 && iterations % 5 == 0) { println("Remember to type 'play' to start.") }
            input = scanner.nextLine()
            iterations++
        }

        playGame()
    }

    private fun whoGoesFirst() : Int { // Simple way of determining who gets to go first
        var scanner = Scanner(System.`in`)
        val randomValue = (1..100).random()
        val player2Guess = (1..100).random()
        var player1Guess: Int

        println("Guess a number between 1 and 100. Player with the closest guess goes first.")
        while (true) { // This will keep running until a proper input is given, where the loop will break
            try {
                player1Guess = scanner.nextInt()
                if (player1Guess < 1 || player1Guess > 100) {
                    throw IllegalArgumentException()
                }
            } catch (e: InputMismatchException) {
                if (scanner.nextLine() == "quit") {
                    println("Goodbye!")
                    exitProcess(0)
                }

                println("Please enter an integer between 1 and 100.")
                scanner = Scanner(System.`in`) // Prevents infinite looping
                continue
            } catch (e: IllegalArgumentException) {
                println("Your number is either too high or too low.")
                continue
            }
            break // If the code reaches here, it means the input was successful
        }

        println("Your guess: $player1Guess")
        println("Player 2 guess: $player2Guess")
        println("The number was... $randomValue")

        val p1Distance = abs(randomValue - player1Guess)
        val p2Distance = abs(randomValue - player2Guess)

        if (p1Distance < p2Distance) {
            println("Player 1 goes first!")
            return 1
        } else if (p2Distance < p1Distance) {
            println("Player 2 goes first!")
            return 2
        } else {
            println("The guesses are tied! So player 1 goes first!")
        }
        return 1 // Player 1 goes first.
    }

    private fun playGame() { // Starts the game
        playerOne = HumanPlayer(1, deck, pile)
        playerTwo = ComputerPlayer(2, deck, pile)

        var rounds = 0

        while (playerOneScore < 50 && playerTwoScore < 50) {
            playerOne.clearHand()
            playerTwo.clearHand()

            pile.clearDeck()

            deck.refill()
            deck.shuffle()

            for (i in 1..5) {
                playerOne.drawOneCard()

                playerTwo.drawOneCard()
            }

            var latestCard = deck.drawCard()

            while (latestCard.cardValue == '8') { // This is to make sure an 8 is not the starting card
                deck.returnCard(latestCard)
                latestCard = deck.drawCard()
            }

            pile.addCard(latestCard) // Add the card to the pile

            playerOne.setRoundScore(0)
            playerTwo.setRoundScore(0)

            val first = whoGoesFirst()

            println("Begin round ${rounds + 1}!")

            if (first == 1) {
                while (!playerOne.isHandEmpty() && !playerTwo.isHandEmpty()) {
                    latestSuitIfEight = if (latestSuitIfEight != "") { // These code blocks check if there is a latest suit
                                                                       // due to an eight and calls playRound accordingly
                        playerOne.playRound(true, latestSuitIfEight)
                    } else {
                        playerOne.playRound()
                    }

                    if (playerOne.isHandEmpty() || playerTwo.isHandEmpty()) { // Mid loop check
                        break
                    }

                    latestSuitIfEight = if (latestSuitIfEight != "") {
                        playerTwo.playRound(true, latestSuitIfEight)
                    } else {
                        playerTwo.playRound()
                    }
                }
            } else if (first == 2) {
                while (!playerOne.isHandEmpty() && !playerTwo.isHandEmpty()) {
                    latestSuitIfEight = if (latestSuitIfEight != "") {
                        playerTwo.playRound(true, latestSuitIfEight)
                    } else {
                        playerTwo.playRound()
                    }

                    if (playerOne.isHandEmpty() || playerTwo.isHandEmpty()) { // Mid loop check
                        break
                    }

                    latestSuitIfEight = if (latestSuitIfEight != "") {
                        playerOne.playRound(true, latestSuitIfEight)
                    } else {
                        playerOne.playRound()
                    }
                }
            }

            println("End of round ${rounds + 1}!")

            println("Calculating scores...")

            val p1RScore = playerOne.calcRoundScore()
            val p2RScore = playerTwo.calcRoundScore()

            playerOneScore += p1RScore
            playerTwoScore += p2RScore

            Thread.sleep(1300)

            println("\nPlayer 1 round score: $ $p1RScore, total score: $playerOneScore")

            Thread.sleep(1300)

            println("\nPlayer 2 round score: $ $p2RScore, total score: $playerTwoScore\n")

            rounds++
        }

        println("A player has reached 50 points! Let's see the final tally...")

        Thread.sleep(3000)

        println("\nPlayer 1 final score: $playerOneScore")

        Thread.sleep(1000)

        println("\nPlayer 2 final score: $playerTwoScore")

        Thread.sleep(2000)

        if (playerOneScore < playerTwoScore) {
            println("Player 1 has the lower score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 ▄█░ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ░█░ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 ▄█▄ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (playerTwoScore < playerOneScore) {
            println("Player 2 has the lower score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 █▀█ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ░▄▀ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 █▄▄ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else {
            println("The scores are equal!")
            Thread.sleep(2000)
            println("\n" +
                    "▀█▀ ▀▀█▀▀ █ ▒█▀▀▀█ 　 ░█▀▀█ 　 ▒█▀▀▄ ▒█▀▀█ ░█▀▀█ ▒█░░▒█ █ \n" +
                    "▒█░ ░▒█░░ ░ ░▀▀▀▄▄ 　 ▒█▄▄█ 　 ▒█░▒█ ▒█▄▄▀ ▒█▄▄█ ▒█▒█▒█ ▀ \n" +
                    "▄█▄ ░▒█░░ ░ ▒█▄▄▄█ 　 ▒█░▒█ 　 ▒█▄▄▀ ▒█░▒█ ▒█░▒█ ▒█▄▀▄█ ▄")
        }

        println("Total rounds: $rounds")
    }
}