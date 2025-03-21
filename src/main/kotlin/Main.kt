import java.util.Scanner
import kotlin.system.exitProcess

fun main() {
    println("Make sure you've read the README so you know how to play!")

    do {
        CrazyEights()

        // All code after this runs when a game ends

        println("\nThank you for playing!")
        println("Want to play again? Type 'yes' or 'y'. Type 'no', 'n', or 'quit' to quit.")

        val scanner = Scanner(System.`in`)

        while (true) {
            val response = scanner.nextLine()
            if (response == "yes" || response == "y") {
                break
            } else if (response == "no" || response == "n" || response == "quit") {
                println("Goodbye!")
                exitProcess(0)
            } else {
                println("Type 'yes'/'y' to play again or 'no'/'n'/'quit' to quit.")
            }
        }
    } while (true)
}