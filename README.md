# Crazy Eights Mini-project

This is a mini-project I decided to make in order to practice using Kotlin. It's a
very simple program that simulates a simple game of Crazy Eights between a human player (you) and
the computer. Perhaps I may expand this project further to see what else I can add/modify.
Below are the basic rules and instructions on how to play.

## How to play

Watch this [short video](https://www.youtube.com/watch?v=1c4YPQTS35I) for the general rules.
However, I made a few mods to the rules.

1. The pull limit per turn is unlimited. So if you pull 20 cards in a single turn, tough luck.
2. I set the score limit to 50 instead of 100, since I wanted games to end faster. I may add a custom score limit feature in the future.
3. Consequently, 8's are worth 25 points instead of 50 so that games don't end in 1 round.
4. I use a number guessing game to determine who goes first.

I hope I did not forget anything else. Please let me know if I did.

## Specific Instructions

At certain points during the game, you will need to type in a specific manner for the game to work.
For example:

1. When you choose a card, you must follow a [value] then [suit] format. It must also only be two words. You also must pick a card you actually have. Also, all spaces before the first word and after the last are ignored, as are any extra spaces between words. Some examples:
   1. 'ten spade' is good
   2. 'k clubs' is good
   3. 'club 3' is bad
   4. 'one quen' is bad
   5. 'Jersey Mike's >>>>>>>>>>>>>> Subway' is true, but a bad answer
2. When you place down an 8, you must pick a suit. This is simple, just type one of the four suits, capitalization does not matter, can be singular or plural, one word, no need for spaces. You can also just type the first letter of a suit. Examples:
   1. 'spade' is good
   2. 'DIAMONDS' is good
   3. 'H' is good
   4. 'crunchwrap supreme' is tasty, but not a valid response
   5. ' clubs ' is invalid
3. At any point when you are typing, you can type 'quit' to end the program.

I believe that should be it. Everything else is pretty self-explanatory.

I hope you enjoy my basic but fun mini-project.

## Things I'm considering adding/changing

- Option for more than 2 players
- Option to edit win score
- Giving certain cards special abilities or adding special cards (+2, swap hands, reverse order, something new, etc.)
- Improved computer decision-making
- Window with UI instead of using the console

## Changelog

- 3/21/25
  - v1.0.0: Initial version
- 3/25/25
  - v1.1.0
    - Added option to simulate game between 2 computers
