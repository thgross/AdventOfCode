package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 extends Application {

    String inputFilename = "input22.txt";

    public static void main(String[] args) {
        var app = (new Day22());
        app.run(app.inputFilename);
    }

    Map<String, Long> snums = new HashMap<>();

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        for (int i = 0; i < lines.size(); i++) {
            snums.put(lines.get(i), 0L);
        }

        long snumSum = 0;
        // Part 1

        for (String firstSecret : snums.keySet()) {
            Long result = Long.parseLong(firstSecret);
            for (int i = 0; i < 2000; i++) {
                result = calcNext(result);
            }
            snums.put(firstSecret, result);
        }

        System.out.println("------------------------------------");
        for (String firstSecret : snums.keySet()) {
            System.out.printf("%s: %d\n", firstSecret, snums.get(firstSecret));
            snumSum+= snums.get(firstSecret);
        }
        System.out.printf("Part 1 Secret Number Sums: %d\n", snumSum);
    }

    long calcNext(long snum) {
        long newnum = snum;
        // mul 64
        long mul64 = newnum << 6;
        // mix
        newnum = mul64 ^ newnum;
        // prune
        newnum = newnum % 16777216L;

        // div 32
        long div32 = newnum >> 5;
        // mix
        newnum = div32 ^ newnum;
        // prune
        newnum = newnum % 16777216L;

        // mul 2048
        long mul2048 = newnum << 11;
        // mix
        newnum = mul2048 ^ newnum;
        // prune
        newnum = newnum % 16777216L;

        return newnum;
    }
}
