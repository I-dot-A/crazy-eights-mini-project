data class Card(private val suit: String, private val value: Char) {
    val cardSuit: String
        get() = suit

    val cardValue: Char
        get() = value

    fun endRoundValue(): Int { // For calculating score at the end of rounds
        return when (value) {
            '8' -> 25
            'K', 'Q', 'J', 'T' -> 10
            'A' -> 1
            else -> value.digitToInt()
        }
    }

    fun cardCode(): String { // Makes extracting cards from hands easier
        var output: String = value.toString()

        output += when (suit) {
            "Spades" -> "S"
            "Diamonds" -> "D"
            "Hearts" -> "H"
            "Clubs" -> "C"
            else -> "LOOOOOOOOOOOOOL I DON'T KNOW HOW YOU GET HERE"
        }

        return output
    }

    private fun valueAsString(): String { // ToString helper function
        return when (value) {
            'A' -> "Ace"
            'T' -> "10"
            'J' -> "Jack"
            'Q' -> "Queen"
            'K' -> "King"
            else -> value.toString()
        }
    }

    override fun toString(): String {
        return "${valueAsString()} of $suit"
    }
}