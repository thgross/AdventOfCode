package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
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
        long part2InvalidIdSum = 0;

        long from, to, range, rangeNr;
        int anzahlStellen;
        List<Integer> digitsTeiler;
        long digitDivisor;

        var ranges = lines.getFirst().split(",");
        rangeNr = 0;
        for (String rangeString : ranges) {
            var rangeParts = rangeString.split("-");
            from = Long.parseLong(rangeParts[0]);
            to = Long.parseLong(rangeParts[1]);
            range = to - from + 1;

//            System.out.printf("Range %3d: %d to %d (%d) | ", rangeNr, from, to, range);

            for (long i = from; i <= to; i++) {

                anzahlStellen = getAnzahlStellen(i);
                digitsTeiler = teilerGroesser1(anzahlStellen);
                if (!digitsTeiler.isEmpty()) {

                    // Damit keine ID doppelt gezählt wird (z.B. bei 222222: 6*2, 3*22, 2*222)
                    long lastInvalidId = 0;

                    // Part 2
                    for (Integer teiler : digitsTeiler) {
                        long itmp = i;
                        digitDivisor = getDigitDivisor(anzahlStellen / teiler);
                        long firstPart = i % digitDivisor;
                        if(firstPart == 0) {continue;}
                        boolean partsSame = true;
                        for (int part = 1; part < teiler; part++) {
                            itmp = itmp / digitDivisor;
                            if (itmp % digitDivisor != firstPart) {
                                partsSame = false;
                                break;
                            }
                        }
                        if(partsSame && lastInvalidId == 0) {
                            lastInvalidId = i;
                            part2InvalidIdSum += i;
//                            System.out.printf("%s -> %d: (%d * %d)\n", rangeString, i, teiler, firstPart);
                        }
                    }
                    // Part 1
                    if (digitsTeiler.getFirst() == 2) {
                        digitDivisor = getDigitDivisor(anzahlStellen / 2);
                        if (i / digitDivisor == i % digitDivisor) {
                            // Hier haben wir eine invalid ID
                            part1InvalidIdSum += i;
                        }
                    }

                }

                // Part 2
                // Wir müssen zuerst alle Teiler der Stellenzahl ermitteln.
//                var alleTeiler = teiler(i);
//                for (Long t : alleTeiler) {

//                }
                // TODO: implementieren
            }

            rangeNr++;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 invalid ID Sum: %d\n", part1InvalidIdSum);
        System.out.printf("Part 2 invalid ID Sum: %d\n", part2InvalidIdSum);
    }

    // ermittelt die Anzahl Stellen, wenn diese gerade ist. Bei ungerader Stellenanzahl wird 0 zurückgegeben.
    int getAnzahlStellen(long num) {
        if (num >= 0L) if (num < 10L) return 1;
        if (num >= 10L) if (num < 100L) return 2;
        if (num >= 100L) if (num < 1000L) return 3;
        if (num >= 1000L) if (num < 10000L) return 4;
        if (num >= 10000L) if (num < 100000L) return 5;
        if (num >= 100000L) if (num < 1000000L) return 6;
        if (num >= 1000000L) if (num < 10000000L) return 7;
        if (num >= 10000000L) if (num < 100000000L) return 8;
        if (num >= 100000000L) if (num < 1000000000L) return 9;
        if (num >= 1000000000L) if (num < 10000000000L) return 10;
        if (num >= 10000000000L) if (num < 100000000000L) return 11;
        if (num >= 100000000000L) if (num < 1000000000000L) return 12;
        if (num >= 1000000000000L) if (num < 10000000000000L) return 13;
        if (num >= 10000000000000L) if (num < 100000000000000L) return 14;
        return 0;
    }

    List<Integer> teilerGroesser1(int zahl) {
        List<Integer> teiler = new ArrayList<>();
        for (int i = 2; i <= zahl; i++) {
            if (zahl % i == 0) {
                teiler.add(i);
            }
        }
        return teiler;
    }

    // Gibt den Divisor zurück für eine bestimmte Anzah Stellen. Damit kann man zum Beispiel den oberen und den unteren
    // Teil einer 6-stelligen Zahl ermitteln, wenn man als `digits` 3 übergibt. Die Methode gibt bei einem Parameter von
    // 3 eine 1000 zurück, welche für Integer-Division bzw. Modulo verwendet werden kann.
    long getDigitDivisor(int digits) {
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
