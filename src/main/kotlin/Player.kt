open class Player(protected val playerNumber: Int, protected val deck: Deck, protected val pile: Deck) { // This class is a superclass for both HumanPlayer and ComputerPlayer
    protected val hand: Deck = Deck(true)
    protected var chosenSuit = ""
    private var roundScore = 0

    val playerRoundScore: Int
        get() = roundScore

    val pNum: Int
        get() = playerNumber

    fun clearHand() {
        hand.clearDeck()
    }

    fun setRoundScore(value: Int) {
        roundScore = value
    }

    fun isHandEmpty() : Boolean {
        return hand.isDeckEmpty()
    }

    fun drawOneCard() {
        hand.addCard(deck.drawCard())
    }

    fun handToPile(card: Card) {
        pile.addCard(hand.take(card))
    }

    fun checkHand(latest: Card) : Boolean { // Returns true if player has a matching suit/value, or an 8
        for (card in hand) {
            if (   card.cardValue == '8'
                || card.cardValue == latest.cardValue
                || card.cardSuit == latest.cardSuit) {
                return true
            }
        }
        return false
    }

    fun calcRoundScore() : Int {
        for (card in hand) {
            roundScore += card.endRoundValue()
        }

        return roundScore
    }

    fun reshufflePileToDeck() { // Takes all cards from pile except top card and puts it in deck, then shuffles deck
        while (pile.numCards() > 1) {
            deck.addCard(pile.drawCardFromBottom())
        }

        deck.shuffle()
    }
}