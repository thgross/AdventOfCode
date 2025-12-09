package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day02 extends Application {

    String inputFilename = "aoc2025/input02.txt";

    public static void main(String[] args) {
        var app = (new Day02());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1InvalidIdSum = 0;

        long from, to, range, rangeNr;
        int evenDigits;
        long digitDiv;

        var ranges = lines.getFirst().split(",");
        rangeNr = 0;
        for (String rangeString : ranges) {
            var rangeParts = rangeString.split("-");
            from = Long.parseLong(rangeParts[0]);
            to = Long.parseLong(rangeParts[1]);
            range = to - from + 1;

//            System.out.printf("Range %3d: %d to %d (%d) | ", rangeNr, from, to, range);

            if (getEvenDigits(from) > 0 || getEvenDigits(to) > 0) {
                for (long i = from; i <= to; i++) {
                    evenDigits = getEvenDigits(i);
                    if (evenDigits > 0) {
                        digitDiv = getDigitDiv(evenDigits / 2);
                        if (i / digitDiv == i % digitDiv) {
                            // Hier haben wir eine invalid ID
                            part1InvalidIdSum += i;
                        }
                    }
                }
            }

            rangeNr++;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 invalid ID Sum: %d\n", part1InvalidIdSum);
    }

    int getEvenDigits(long num) {
        if (num >= 10L) if (num < 100L) return 2;
        if (num >= 1000L) if (num < 10000L) return 4;
        if (num >= 100000L) if (num < 1000000L) return 6;
        if (num >= 10000000L) if (num < 100000000L) return 8;
        if (num >= 1000000000L) if (num < 10000000000L) return 10;
        if (num >= 100000000000L) if (num < 1000000000000L) return 12;
        if (num >= 10000000000000L) if (num < 100000000000000L) return 14;
        return 0;
    }

    long getDigitDiv(int digits) {
// Array speichert 10^0 bis 10^18. Der Index entspricht dem Exponenten.
        // Index i speichert 10^i. Wir benötigen 10^(digits).
        final long[] powerOf10 = {
                1L,          // 10^0
                10L,         // 10^1
                100L,        // 10^2
                1000L,       // 10^3
                10000L,      // 10^4
                100000L,     // 10^5
                1000000L,    // 10^6
                10000000L,   // 10^7
                100000000L,  // 10^8
                1000000000L, // 10^9
                10000000000L, // 10^10 (passt nicht mehr in int!)
                100000000000L, 1000000000000L, 10000000000000L, 100000000000000L,
                1000000000000000L, 10000000000000000L, 100000000000000000L,
                1000000000000000000L // 10^18
        };

        // Die maximale Anzahl der Stellen für einen Long ist 19.
        // Wir überprüfen, ob die Eingabe im gültigen Bereich liegt.
        if (digits < 1 || digits > 19) {
            throw new IllegalArgumentException("Die Anzahl der Stellen muss zwischen 1 und 19 liegen.");
        }

        // Der Exponent, den wir benötigen, ist (digits).

        return powerOf10[digits];
    }
}
