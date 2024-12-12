package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class AOC2024_11 extends Application {
    public static void main(String[] args) {
        (new AOC2024_11()).run();
    }

    public static final int MAX_MAPSIZE = Integer.MAX_VALUE / 4;

    static class Pdata {
        long[] stones = new long[MAX_MAPSIZE];
        int sc = 0;
        int blinks1 = 25;
        int blinks2 = 50;
        long stoneCount1;
        long stoneCount2;
        Instant start;
        Instant stop;
    }

    private final AOC2024_11.Pdata lc = new AOC2024_11.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("input11.txt");
            lc.stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var content = getFileContent(inputFile);

        // Parsen
        for (String s : content.split(" ")) {
            lc.stones[lc.sc++] = Long.parseLong(s);
        }

        for (int blink = 0; blink < lc.blinks1; blink++) {
            var istart = Instant.now();
            blink(lc.stones, lc.sc);
            System.out.printf("Blink %d: %d Items (%s)\n", blink, lc.sc, Duration.between(istart, Instant.now()));
            dump(lc.stones, lc.sc);
        }
        lc.stoneCount1 = lc.sc;

        for (int blink = 0; blink < lc.blinks2; blink++) {
            var istart = Instant.now();
            blink(lc.stones, lc.sc);
            System.out.printf("Blink %d: %d Items (%s)\n", lc.blinks1 + blink, lc.sc, Duration.between(istart, Instant.now()));
        }
        lc.stoneCount2 = lc.sc;

        System.out.printf("Stones after %d blinks: %d\n", lc.blinks1, lc.stoneCount1);
        System.out.printf("Stones after %d blinks: %d\n", lc.blinks1 + lc.blinks2, lc.stoneCount2);
    }

    private void dump(long[] stones, int sc) {
        for (int i = 0; i < sc; i++) {
            System.out.print(stones[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    private void blink(long[] stones, int sc) {

        for (int i = 0; i < sc; i++) {
            var num = stones[i];
            if (num == 0) {
                stones[i] = 1L;

            } else if (String.valueOf(num).length() % 2 == 0) {
                // siehe https://stackoverflow.com/a/1306751/4576064
                var sval = String.valueOf(num);
                stones[i] = Long.parseLong(sval.substring(0, sval.length() / 2));
                stones[lc.sc++] = Long.parseLong(sval.substring(sval.length() / 2));
            } else {
                stones[i] = num * 2024;
            }
        }
    }
}
