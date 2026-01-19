package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day03 extends Application {

    String inputFilename = "aoc2025/input03.txt";
    private static final long[] POW10 = {1, 10, 100, 1000, 10000, 100000, 1000000};

    public static void main(String[] args) {
        var app = (new Day03());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1joltage = 0;

        for (String line : lines) {
            int[] bank = line.chars()
                    .map(Character::getNumericValue)
                    .toArray();
            long linejoltage = getMaxJoltage(bank, 2);
//            System.out.printf("Line: %s | Joltage: %d\n", line, linejoltage);
            part1joltage += linejoltage;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 max joltage: %d\n", part1joltage);
    }

    private long getMaxJoltage(int[] bank, @SuppressWarnings("SameParameterValue") int numDigits) {
        long joltage = 0;
        int bpos = 0;
        for (int digit = 0; digit < numDigits; digit++) {
            long digitval = 0;
            // suchen zwischen bpos und bank.size - numDigits + digit
            for (int n = bpos; n < bank.length - numDigits + digit + 1; n++) {
                if (bank[n] > digitval) {
                    digitval = bank[n];
                    bpos = n + 1;
                }
                if (digitval == 9) {
                    break;
                }
            }
            joltage += digitval * POW10[numDigits - digit - 1];
        }

        return joltage;
    }
}
