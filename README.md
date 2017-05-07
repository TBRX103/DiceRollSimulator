## Dice Roll Simulator

Recently bought myself a new processor (Ryzen 1700X) and was looking for a good way to work on something interesting to take advantage of its power that I could understand and see.

I was also playing Zero Time Dilemma of the Zero Escape series at the time, and part of one of the segments in the game requires 3 characters to each roll a die which all must roll a 1. The probability of this occurance was 0.46% and seemed to make sense without doing the math.

Then, a character stated the probability for rolling 10 dice with the same 60,466,176:1. Bingo. Inspiration.

Thus, this multi-threaded simulator was born.

I've written the code to be simple to understand and take advantage of multithreading and hopefully is a useful resource for other curious minds.

This code is released under the MIT License.