import java.util.InputMismatchException
import java.util.Scanner
import kotlin.IllegalArgumentException
import kotlin.math.abs
import kotlin.system.exitProcess

class CrazyEights {
    private val deck = Deck(true)
    private val pile = Deck(true)
    private val players: ArrayList<Player> = ArrayList()
    private var numPlayers = 2
    private var latestSuitIfEight : String = ""
    private var playerOneScore = 0
    private var playerTwoScore = 0
    private var playerThreeScore = 0
    private var playerFourScore = 0
    private var playerFiveScore = 0
    private var humanPlaying = true
    private var scoreLimit = 50

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
                Game scoring limit: $scoreLimit
                Number of players: $numPlayers
            """.trimIndent())
    }

    private fun editRules() {
        println("-----".repeat(20))
        printRules()
        var scanner = Scanner(System.`in`)
        while (true) {
            println("Options: 'toggle human' / 'edit score' / 'num players' / 'exit'")

            val input = scanner.nextLine()
            val inputLC = input.lowercase()

            if (inputLC == "toggle human" || inputLC == "togglehuman" || inputLC == "toggle") {
                humanPlaying = !humanPlaying
                println("Human player setting toggled.\n")
                printRules()
            } else if (inputLC == "edit score" || inputLC == "editscore" || inputLC == "score") {
                print("Enter a new integer score limit. Must be at least 1: ")

                while (true) {
                    try {
                        val newInt = scanner.nextInt()

                        if (newInt < 1) {
                            println("Your integer is too low!")
                            continue
                        } else {
                            scoreLimit = newInt
                            println("New score limit set.\n")
                            scanner = Scanner(System.`in`)
                            break
                        }
                    } catch (e: InputMismatchException) {
                        println("Bad input. Please enter an integer.")
                        scanner = Scanner(System.`in`)
                        continue
                    }
                }

                printRules()
            } else if (inputLC == "num players" || inputLC == "numplayers" || inputLC == "players") {
                print("Enter a new player count. Must be at least 2 and no more than 5: ")

                while (true) {
                    try {
                        val newPCount = scanner.nextInt()

                        if (newPCount < 2) {
                            println("Too low!")
                            continue
                        } else {
                            numPlayers = newPCount
                            println("New player count set.\n")
                            scanner = Scanner(System.`in`)
                            break
                        }
                    } catch (e: InputMismatchException) {
                        println("Bad input. Please enter an integer.")
                        scanner = Scanner(System.`in`)
                        continue
                    }
                }

                printRules()
            } else if (inputLC == "exit") {
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

        // These are set extremely high, so they cannot be the closest guess unless they're set to 1 <= num <= 100
        var player3Guess = 99999999
        var player4Guess = 99999999
        var player5Guess = 99999999

        if (numPlayers > 2)
            player3Guess = (1..100).random()

        if (numPlayers > 3)
            player4Guess = (1..100).random()

        if (numPlayers > 4)
            player5Guess = (1..100).random()

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

        if (numPlayers > 2)
            println("Player 3 guess: $player3Guess")

        if (numPlayers > 3)
            println("Player 4 guess: $player4Guess")

        if (numPlayers > 4)
            println("Player 5 guess: $player5Guess")

        println("The number was... $randomValue")

        val p1Distance = abs(randomValue - player1Guess)
        val p2Distance = abs(randomValue - player2Guess)
        val p3Distance = abs(randomValue - player2Guess)
        val p4Distance = abs(randomValue - player2Guess)
        val p5Distance = abs(randomValue - player2Guess)

        val minGuess = listOf(p1Distance, p2Distance,
            p3Distance, p4Distance, p5Distance).min() // Find the closest guess

        when (minGuess) {
            p1Distance -> {
                println("Player 1 goes first!")
                return 1
            }
            p2Distance -> {
                println("Player 2 goes first!")
                return 2
            }
            p3Distance -> {
                println("Player 3 goes first!")
                return 3
            }
            p4Distance -> {
                println("Player 4 goes first!")
                return 4
            }
            p5Distance -> {
                println("Player 5 goes first!")
                return 5
            }
            else -> {
                println("The guesses are tied! So player 1 goes first!")
            }
        }

        return 1 // Player 1 goes first.
    }

    private fun noHandsEmpty() : Boolean {
        return players.take(numPlayers).all { !it.isHandEmpty() }
    }

    private fun allUnderScoreLimit() : Boolean {
        val scores = listOf(playerOneScore, playerTwoScore, playerThreeScore, playerFourScore, playerFiveScore)
        return scores.all { it < scoreLimit }
    }

    private fun playAllRounds(computerPlayers: ArrayList<ComputerPlayer>, hPlayer: HumanPlayer? = null, hPlayerTurnNum: Int = -1) {
        if (!humanPlaying)
            println("Player ${computerPlayers[0].pNum} goes first.")

        outer@ while (noHandsEmpty()) {
            var j = 0
            for (i in 0..< numPlayers) {
                latestSuitIfEight = if (latestSuitIfEight != "") { // These code blocks check if there is a latest suit
                    // due to an eight and calls playRound accordingly
                    if (i + 1 == hPlayerTurnNum && hPlayer != null) {
                        hPlayer.playRound(true, latestSuitIfEight)
                    } else {
                        computerPlayers[j].playRound(true, latestSuitIfEight)
                    }
                } else {
                    if (i + 1 == hPlayerTurnNum && hPlayer != null) {
                        hPlayer.playRound()
                    } else {
                        computerPlayers[j].playRound()
                    }
                }

                if (i + 1 != hPlayerTurnNum)
                    j++

                if (!noHandsEmpty())
                    break@outer
            }
        }
    }

    private fun isTie(lowestScore: Int) : Boolean {
        val temp = arrayOf(playerOneScore, playerTwoScore, playerThreeScore,
            playerFourScore, playerFiveScore)

        for (i in 0..4) {
            for (j in i + 1..4) {
                if (temp[i] == lowestScore && temp[i] == temp[j])
                    return true
            }
        }

        return false
    }

    private fun playGame() { // Starts the game
        // Variables for casting
        lateinit var humanP1 : HumanPlayer
        lateinit var computerP1 : ComputerPlayer
        lateinit var p2 : ComputerPlayer
        lateinit var p3 : ComputerPlayer
        lateinit var p4 : ComputerPlayer
        lateinit var p5 : ComputerPlayer

        if (humanPlaying) {
            players.add(HumanPlayer(1, deck, pile))

            for (i in 2..numPlayers) {
                players.add(ComputerPlayer(i, deck, pile))
            }

            humanP1 = players[0] as HumanPlayer
            p2 = players[1] as ComputerPlayer

            for (i in 2..< numPlayers) {
                when (i) {
                    2 -> p3 = players[2] as ComputerPlayer
                    3 -> p4 = players[3] as ComputerPlayer
                    4 -> p5 = players[4] as ComputerPlayer
                }
            }

        } else {
            for (i in 1..numPlayers) {
                players.add(ComputerPlayer(i, deck, pile, true))
            }

            for (i in 0..< numPlayers) {
                when (i) {
                    0 -> computerP1 = players[0] as ComputerPlayer
                    1 -> p2 = players[1] as ComputerPlayer
                    2 -> p3 = players[2] as ComputerPlayer
                    3 -> p4 = players[3] as ComputerPlayer
                    4 -> p5 = players[4] as ComputerPlayer
                }
            }

            println("Human player is set to false! Simulating game...")
            Thread.sleep(2000)
        }

        var rounds = 0

        while (allUnderScoreLimit()) {
            players[0].clearHand() // Clears hand regardless if player 1 is a human or computer
            p2.clearHand()

            for (i in 2..< numPlayers) {
                when (i) {
                    2 -> p3.clearHand()
                    3 -> p4.clearHand()
                    4 -> p5.clearHand()
                }
            }

            pile.clearDeck()

            deck.refill()
            deck.shuffle()

            for (i in 1..5) {
                players[0].drawOneCard()
                p2.drawOneCard()

                for (j in 2..< numPlayers) {
                    when (j) {
                        2 -> p3.drawOneCard()
                        3 -> p4.drawOneCard()
                        4 -> p5.drawOneCard()
                    }
                }
            }

            var latestCard = deck.drawCard()

            while (latestCard.cardValue == '8') { // This is to make sure an 8 is not the starting card
                deck.returnCard(latestCard)
                latestCard = deck.drawCard()
            }

            pile.addCard(latestCard) // Add the card to the pile

            players[0].setRoundScore(0)
            p2.setRoundScore(0)

            for (i in 2..< numPlayers) {
                when (i) {
                    2 -> p3.setRoundScore(0)
                    3 -> p4.setRoundScore(0)
                    4 -> p5.setRoundScore(0)
                }
            }

            val first : Int = if (humanPlaying)
                whoGoesFirst()
            else
                (1..numPlayers).random() // Randomly picks between 1 and numPlayers

            println("Begin round ${rounds + 1}!")

            val orderOfCompTurns: ArrayList<ComputerPlayer> = if (humanPlaying) {
                when (Pair(first, numPlayers)) {
                    Pair(1, 2) -> arrayListOf(p2)
                    Pair(2, 2) -> arrayListOf(p2)
                    Pair(1, 3) -> arrayListOf(p2, p3)
                    Pair(2, 3) -> arrayListOf(p2, p3)
                    Pair(3, 3) -> arrayListOf(p3, p2)
                    Pair(1, 4) -> arrayListOf(p2, p3, p4)
                    Pair(2, 4) -> arrayListOf(p2, p3, p4)
                    Pair(3, 4) -> arrayListOf(p3, p4, p2)
                    Pair(4, 4) -> arrayListOf(p4, p2, p3)
                    Pair(1, 5) -> arrayListOf(p2, p3, p4, p5)
                    Pair(2, 5) -> arrayListOf(p2, p3, p4, p5)
                    Pair(3, 5) -> arrayListOf(p3, p4, p5, p2)
                    Pair(4, 5) -> arrayListOf(p4, p5, p2, p3)
                    else -> arrayListOf(p5, p2, p3, p4)
                }
            } else {
                when (Pair(first, numPlayers)) {
                    Pair(1, 2) -> arrayListOf(computerP1, p2)
                    Pair(2, 2) -> arrayListOf(p2, computerP1)
                    Pair(1, 3) -> arrayListOf(computerP1, p2, p3)
                    Pair(2, 3) -> arrayListOf(p2, p3, computerP1)
                    Pair(3, 3) -> arrayListOf(p3, computerP1, p2)
                    Pair(1, 4) -> arrayListOf(computerP1, p2, p3, p4)
                    Pair(2, 4) -> arrayListOf(p2, p3, p4, computerP1)
                    Pair(3, 4) -> arrayListOf(p3, p4, computerP1, p2)
                    Pair(4, 4) -> arrayListOf(p4, computerP1, p2, p3)
                    Pair(1, 5) -> arrayListOf(computerP1, p2, p3, p4, p5)
                    Pair(2, 5) -> arrayListOf(p2, p3, p4, p5, computerP1)
                    Pair(3, 5) -> arrayListOf(p3, p4, p5, computerP1, p2)
                    Pair(4, 5) -> arrayListOf(p4, p5, computerP1, p2, p3)
                    else -> arrayListOf(p5, computerP1, p2, p3, p4)
                }
            }

            val humanTurn = when (first) {
                1 -> 1
                2 -> numPlayers // Guarantees last
                3 -> numPlayers - 1
                4 -> numPlayers - 2
                else -> numPlayers - 3
            }

            if (humanPlaying)
                playAllRounds(orderOfCompTurns, humanP1, humanTurn)
            else
                playAllRounds(orderOfCompTurns)

            println("End of round ${rounds + 1}!")

            println("Calculating scores...")

            players[0].calcRoundScore()
            p2.calcRoundScore()

            playerOneScore += players[0].playerRoundScore
            playerTwoScore += p2.playerRoundScore

            for (i in 2..< numPlayers) {
                when (i) {
                    2 -> {
                        p3.calcRoundScore()
                        playerThreeScore += p3.playerRoundScore
                    }
                    3 -> {
                        p4.calcRoundScore()
                        playerFourScore += p4.playerRoundScore
                    }
                    4 -> {
                        p5.calcRoundScore()
                        playerFiveScore += p5.playerRoundScore
                    }
                }
            }

            Thread.sleep(1300)

            println("\nPlayer 1 round score: $ ${players[0].playerRoundScore}, total score: $playerOneScore")

            Thread.sleep(1000)

            println("\nPlayer 2 round score: $ ${p2.playerRoundScore}, total score: $playerTwoScore")

            for (i in 2..< numPlayers) {
                when (i) {
                    2 -> {
                        Thread.sleep(1000)
                        println("\nPlayer 3 round score: $ ${p3.playerRoundScore}, total score: $playerThreeScore")
                    }
                    3 -> {
                        Thread.sleep(1000)
                        println("\nPlayer 4 round score: $ ${p4.playerRoundScore}, total score: $playerFourScore")
                    }
                    4 -> {
                        Thread.sleep(1000)
                        println("\nPlayer 5 round score: $ ${p5.playerRoundScore}, total score: $playerFiveScore")
                    }
                }
            }

            println()

            rounds++
        }

        if (scoreLimit == 1)
            println("A player has reached 1 point! The game's already over...")
        else
            println("A player has reached $scoreLimit points! Let's see the final tally...")

        Thread.sleep(3000)

        println("\nPlayer 1 final score: $playerOneScore")

        Thread.sleep(1000)

        println("\nPlayer 2 final score: $playerTwoScore")

        for (i in 2..< numPlayers) {
            when (i) {
                2 -> {
                    Thread.sleep(1000)
                    println("\nPlayer 3 final score: $playerThreeScore")
                }
                3 -> {
                    Thread.sleep(1000)
                    println("\nPlayer 4 final score: $playerFourScore")
                }
                4 -> {
                    Thread.sleep(1000)
                    println("\nPlayer 5 final score: $playerFiveScore")
                }
            }
        }

        if (numPlayers > 2) { // Makes the unused score variables unrealistically high, so they cannot be the minimum
            playerFourScore = 9999999
            playerFiveScore = 9999999
        } else if (this.numPlayers > 3) {
            playerFiveScore = 9999999
        }

        val lowestScore = arrayOf(playerOneScore, playerTwoScore,
            playerThreeScore, playerFourScore, playerFiveScore).min()

        Thread.sleep(2000)

        if (lowestScore == playerOneScore) {
            println("Player 1 has the lowest score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 ▄█░ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ░█░ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 ▄█▄ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (lowestScore == playerTwoScore) {
            println("Player 2 has the lowest score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 █▀█ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ░▄▀ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 █▄▄ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (lowestScore == playerThreeScore) {
            println("Player 3 has the lowest score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 █▀▀█ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ░░▀▄ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 █▄▄█ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (lowestScore == playerFourScore) {
            println("Player 4 has the lowest score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 ░█▀█░ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 █▄▄█▄ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 ░░░█░ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (lowestScore == playerFiveScore) {
            println("Player 5 has the lowest score...")
            Thread.sleep(2000)
            println("\n" +
                    "▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ 　 █▀▀ 　 ▒█░░▒█ ▀█▀ ▒█▄░▒█ ▒█▀▀▀█ █ \n" +
                    "▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ 　 ▀▀▄ 　 ▒█▒█▒█ ▒█░ ▒█▒█▒█ ░▀▀▀▄▄ ▀ \n" +
                    "▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ 　 ▄▄▀ 　 ▒█▄▀▄█ ▄█▄ ▒█░░▀█ ▒█▄▄▄█ ▄")
        } else if (isTie(lowestScore)) {
            println("At least two players have equal scores!")
            Thread.sleep(2000)
            println("\n" +
                    "▀█▀ ▀▀█▀▀ █ ▒█▀▀▀█ 　 ░█▀▀█ 　 ▒█▀▀▄ ▒█▀▀█ ░█▀▀█ ▒█░░▒█ █ \n" +
                    "▒█░ ░▒█░░ ░ ░▀▀▀▄▄ 　 ▒█▄▄█ 　 ▒█░▒█ ▒█▄▄▀ ▒█▄▄█ ▒█▒█▒█ ▀ \n" +
                    "▄█▄ ░▒█░░ ░ ▒█▄▄▄█ 　 ▒█░▒█ 　 ▒█▄▄▀ ▒█░▒█ ▒█░▒█ ▒█▄▀▄█ ▄")
        } else {
            println("Not sure how we got here...")
        }

        println("Total rounds: $rounds")
    }
}