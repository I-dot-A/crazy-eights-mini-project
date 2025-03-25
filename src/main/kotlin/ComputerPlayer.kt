class ComputerPlayer(playerNumber: Int, deck: Deck, pile: Deck, private val handVisible: Boolean = false) : Player(playerNumber, deck, pile) {
    // There is a protected Deck object called 'hand'
    private val suits = arrayOf("Spades", "Hearts", "Diamonds", "Clubs")

    private fun pickCard(latest: Card) { // This function provides the logic for picking a card
        for (card in hand) { // Favor 8's first
            if (card.cardValue == '8') {
                println("Player $playerNumber plays $card. It's a crazy eight!")
                Thread.sleep(650)

                chosenSuit = suits[(0..3).random()]
                println("Player $playerNumber picks $chosenSuit")
                handToPile(card)
                return
            }
        }

        val oneOrTwo = (0..1).random()

        if (oneOrTwo == 0) { // Randomly pick to search hand forward or backwards
            for (card in hand) {
                if (card.cardValue == latest.cardValue || card.cardSuit == latest.cardSuit) {
                    println("Player $playerNumber plays $card.")
                    Thread.sleep(650)
                    handToPile(card)
                    break
                }
            }
        } else {
            for (i in (hand.numCards() - 1 downTo 0)) {
                if (hand.atIndex(i).cardValue == latest.cardValue || hand.atIndex(i).cardSuit == latest.cardSuit) {
                    println("Player $playerNumber plays ${hand.atIndex(i)}.")
                    Thread.sleep(650)
                    handToPile(hand.atIndex(i))
                    break
                }
            }
        }
    }

    fun playRound(latestEight: Boolean = false, opponentChosenSuit: String = "") : String { // Returns String when an 8 is played
        val latest : Card = if (latestEight)
            Card(opponentChosenSuit, '8') // Temporary card in case latest is an 8
        else
            pile.topOfDeck()

        chosenSuit = ""
        var cardsDrawn = 0

        println("-----".repeat(20))
        println("It is now player $playerNumber's turn.")
        println("Card on pile: $latest")

        if (handVisible)
            println("Player $playerNumber's hand: $hand")

        println("\nPlayer $playerNumber is deciding...")
        Thread.sleep(1000)

        while (!checkHand(latest)) {
            println("\nPlayer $playerNumber does not have a playable card, drawing from deck...")
            Thread.sleep(650)
            if (!deck.isDeckEmpty()) {
                drawOneCard()
                if (handVisible)
                    println("Player $playerNumber's hand: $hand")
            } else {
                println("Deck is empty. Reshuffling...")
                reshufflePileToDeck()
                Thread.sleep(300)
                println("New card taken")
                drawOneCard()
                if (handVisible)
                    println("Player $playerNumber's hand: $hand")
            }
            cardsDrawn++
        }

        if (cardsDrawn > 9)
            println("Ooooooh... $cardsDrawn drawn cards. That's rough...")

        println("\nPlayer $playerNumber has a playable card!")

        Thread.sleep(650)

        pickCard(latest)

        if (handVisible)
            println("Player $playerNumber's hand: $hand")

        return chosenSuit
    }
}