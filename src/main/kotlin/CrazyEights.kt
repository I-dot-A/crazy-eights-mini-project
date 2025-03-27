import java.util.InputMismatchException
import java.util.Scanner
import kotlin.IllegalArgumentException
import kotlin.math.abs
import kotlin.system.exitProcess

class CrazyEights {
    private val deck = Deck(true)
    private val pile = Deck(true)
    private lateinit var p1 : Player
    private lateinit var p2 : Player
    private var latestSuitIfEight : String = ""
    private var playerOneScore = 0
    private var playerTwoScore = 0
    private var humanPlaying = true

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

    private fun printRules() {
        println("""
                --=CURRENT RULES=--
                Human Player: $humanPlaying
            """.trimIndent())
    }

    private fun editRules() {
        println("-----".repeat(20))
        printRules()
        val scanner = Scanner(System.`in`)
        while (true) {
            println("Options: 'toggle human' / 'exit'")

            val input = scanner.nextLine()

            if (input.lowercase() == "toggle human" || input.lowercase() == "togglehuman" || input.lowercase() == "toggle") {
                humanPlaying = !humanPlaying
                printRules()
            } else if (input.lowercase() == "exit") {
                println("Exiting options menu...")
                println("-----".repeat(20))
                break
            } else {
                println("Invalid response.")
            }
        }
    }

    private fun waitForPlayerConfirmation() { // When the program starts, the player needs to type 'play'
        sillyTitleDrop()
        val scanner = Scanner(System.`in`)
        println("Type 'play' to start. Type 'edit' to edit the rules.")

        var input = scanner.nextLine()
        var iterations = 0
        while (input.lowercase() != "play") { // Will keep looping until the player puts in the correct input
            if (input.lowercase() == "quit") {
                println("Goodbye!")
                exitProcess(0)
            } else if (input.lowercase() == "edit") {
                editRules()
                println("Type 'play' to start. Type 'edit' to edit the rules.")
            }

            if (iterations != 0 && iterations % 5 == 0) { println("Remember to type 'play' to start. Type 'edit' to edit the rules.") }
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
        // Variables for casting
        lateinit var humanPlayerOne : HumanPlayer
        lateinit var computerPlayerOne : ComputerPlayer
        lateinit var playerTwo : ComputerPlayer

        if (humanPlaying) {
            p1 = HumanPlayer(1, deck, pile)
            p2 = ComputerPlayer(2, deck, pile)

            humanPlayerOne = p1 as HumanPlayer
            playerTwo = p2 as ComputerPlayer
        } else {
            p1 = ComputerPlayer(1, deck, pile, true)
            p2 = ComputerPlayer(2, deck, pile, true)

            computerPlayerOne = p1 as ComputerPlayer
            playerTwo = p2 as ComputerPlayer

            println("Human player is set to false! Simulating game...")
            Thread.sleep(2000)
        }

        var rounds = 0

        while (playerOneScore < 50 && playerTwoScore < 50) {
            p1.clearHand()
            p2.clearHand()

            pile.clearDeck()

            deck.refill()
            deck.shuffle()

            for (i in 1..5) {
                p1.drawOneCard()

                p2.drawOneCard()
            }

            var latestCard = deck.drawCard()

            while (latestCard.cardValue == '8') { // This is to make sure an 8 is not the starting card
                deck.returnCard(latestCard)
                latestCard = deck.drawCard()
            }

            pile.addCard(latestCard) // Add the card to the pile

            p1.setRoundScore(0)
            p2.setRoundScore(0)

            val first : Int = if (humanPlaying)
                whoGoesFirst()
            else
                (1..2).random() // Randomly picks 1 or 2

            println("Begin round ${rounds + 1}!")

            if (first == 1) {
                if (!humanPlaying)
                    println("Player 1 goes first.")

                while (!p1.isHandEmpty() && !p2.isHandEmpty()) {
                    latestSuitIfEight = if (latestSuitIfEight != "") { // These code blocks check if there is a latest suit
                                                                       // due to an eight and calls playRound accordingly
                        if (humanPlaying)
                            humanPlayerOne.playRound(true, latestSuitIfEight)
                        else
                            computerPlayerOne.playRound(true, latestSuitIfEight)

                    } else {
                        if (humanPlaying)
                            humanPlayerOne.playRound()
                        else
                            computerPlayerOne.playRound()
                    }

                    if (p1.isHandEmpty() || p2.isHandEmpty()) // Mid loop check
                        break

                    latestSuitIfEight = if (latestSuitIfEight != "")
                        playerTwo.playRound(true, latestSuitIfEight)
                    else
                        playerTwo.playRound()
                }
            } else if (first == 2) {
                if (!humanPlaying)
                    println("Player 2 goes first.")

                while (!p1.isHandEmpty() && !p2.isHandEmpty()) {
                    latestSuitIfEight = if (latestSuitIfEight != "")
                        playerTwo.playRound(true, latestSuitIfEight)
                    else
                        playerTwo.playRound()

                    if (p1.isHandEmpty() || p2.isHandEmpty()) // Mid loop check
                        break

                    latestSuitIfEight = if (latestSuitIfEight != "") {
                        if (humanPlaying)
                            humanPlayerOne.playRound(true, latestSuitIfEight)
                        else
                            computerPlayerOne.playRound(true, latestSuitIfEight)

                    } else {
                        if (humanPlaying)
                            humanPlayerOne.playRound()
                        else
                            computerPlayerOne.playRound()
                    }
                }
            }

            println("End of round ${rounds + 1}!")

            println("Calculating scores...")

            val p1RScore = p1.calcRoundScore()
            val p2RScore = p2.calcRoundScore()

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