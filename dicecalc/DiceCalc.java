/*
MIT License

Copyright (c) 2017 Ben Schellenberger

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package dicecalc;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceCalc {

    private static final int DICE_VALUE = 1; //Dice side to match
    private static final AtomicBoolean FINISHED = new AtomicBoolean(false);
    private static final int NUM_ROLLS_TO_MATCH = 10; //# of Dice that must have the same number
    private static final int NUM_SIDED_DIE = 6;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final long SLEEP_CHECK_DELAY_MS = 100;
    private static final List<DiceThread> THREADS = new ArrayList<>();
    private static final AtomicLong TRIES = new AtomicLong(0);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (NUM_ROLLS_TO_MATCH <= 0) {
            System.out.println("No dice set to roll.");
            return;
        }

        if (NUM_SIDED_DIE <= 1) {
            System.out.println("Dice must have more than one side.");
            return;
        }

        if (DICE_VALUE > NUM_SIDED_DIE) {
            System.out.println("Roll-check value is greater than # of sides of dice.");
            return;
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            THREADS.add(new DiceThread());
        }
        THREADS.forEach((t) -> {
            t.start();
        });
        while (!FINISHED.get()) {
            try {
                Thread.sleep(SLEEP_CHECK_DELAY_MS);
            } catch (InterruptedException ex) {
                Logger.getLogger(DiceCalc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Done. " + TRIES.get() + " attempts ");
    }

    static class DiceThread extends Thread {

        final SecureRandom rand = new SecureRandom();
        final int[] rolls = new int[NUM_ROLLS_TO_MATCH];

        @Override
        public void run() {
            while (true) {
                if (FINISHED.get()) {
                    return;
                }
                doRoll();
            }
        }

        private void doRoll() {
            for (int i = 0; i < rolls.length; i++) {
                rolls[i] = rand.nextInt(NUM_SIDED_DIE) + 1;
            }

            for (int i = 0; i < rolls.length; i++) {
                if (rolls[i] != DICE_VALUE) {
                    if (!FINISHED.get()) {
                        TRIES.incrementAndGet();
                    }
                    return;
                }
            }
            TRIES.incrementAndGet();
            FINISHED.set(true);
        }
    }
}
