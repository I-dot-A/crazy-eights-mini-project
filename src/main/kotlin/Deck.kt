class Deck(startEmpty: Boolean) : Iterable<Card> {
    private val cardDeck: ArrayList<Card> = ArrayList()

    init {
        if (!startEmpty) {
            refill()
        }
    }

    fun numCards() : Int {
        return cardDeck.size
    }

    fun atIndex(i: Int) : Card {
        return cardDeck[i]
    }

    fun isDeckEmpty() : Boolean {
        return cardDeck.isEmpty()
    }

    fun clearDeck() {
        cardDeck.clear()
    }

    fun refill() { // Refill deck to full
        cardDeck.clear()

        val suits = arrayOf("Spades", "Diamonds", "Clubs", "Hearts")
        val values = arrayOf('A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K')

        for (suit in suits) {
            for (value in values) {
                cardDeck.add(Card(suit, value))
            }
        }
    }

    fun shuffle() { cardDeck.shuffle() }

    fun drawCard() : Card { // Returns the card on top of the pile, if deck is empty, returns an "empty" card
        if (cardDeck.isNotEmpty()) {
            return cardDeck.removeAt(cardDeck.size - 1)
        }
        return Card("empty", 'e')
    }

    fun drawCardFromBottom() : Card { // Function specifically meant for reshufflePileToDeck
        if (cardDeck.isNotEmpty()) {
            return cardDeck.removeAt(0)
        }
        return Card("empty", 'e')
    }

    fun take(card: Card): Card {
        val cardIndex = cardDeck.indexOf(card)

        return cardDeck.removeAt(cardIndex)
    }

    fun topOfDeck() : Card { // This is like takeCard, but it does not remove the card from the deck
        if (cardDeck.isNotEmpty()) {
            return cardDeck[cardDeck.size - 1]
        }
        return Card("empty", 'e')
    }

    fun addCard(card: Card) { // Adds card to top of deck
        cardDeck.add(card)
    }

    fun returnCard(card : Card) { // Places the card back in the deck in a random spot that is not the top of the deck
        val randomIndex = (0..cardDeck.size - 2).random()

        cardDeck.add(randomIndex, card)
    }

    override fun iterator(): Iterator<Card> {
        return cardDeck.iterator()
    }

    override fun toString(): String {
        return "$cardDeck"
    }
}