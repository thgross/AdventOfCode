package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class AOC2024_11b extends Application {
    public static void main(String[] args) {
        (new AOC2024_11b()).run();
    }

    public static final int MAX_MAPSIZE = Integer.MAX_VALUE / 4;
    public static final int PREPSIZE = 10;
    public static final int PREPBLINKS = 38;

    static class Pdata {
        long[] stones = new long[MAX_MAPSIZE];
        long[][] prepCounts = new long[PREPSIZE][PREPBLINKS + 1];
        long[][] prepStones = new long[PREPSIZE][10000000];
        int sc = 0;
        int blinks1 = 25;
        int blinks2 = 65;
        long stoneCount1;
        long stoneCount2;
        Instant start;
        Instant stop;
    }

    private final AOC2024_11b.Pdata lc = new AOC2024_11b.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("aoc2024/input11.txt");
            lc.stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        Instant _i;

        _i = Instant.now();
        prepare();
        System.out.printf("Runtime prepare(): %s\n", Duration.between(_i, Instant.now()));

        var content = getFileContent(inputFile);

        // Parsen
        for (String s : content.split(" ")) {
            lc.stones[lc.sc++] = Long.parseLong(s);
        }

        _i = Instant.now();
        lc.stoneCount1 = 0;
        for (int i = 0; i < lc.sc; i++) {
            lc.stoneCount1 += calc(lc.stones[i], lc.blinks1);
        }
        System.out.printf("Runtime %d Blinks: %s\n", lc.blinks1, Duration.between(_i, Instant.now()));

        _i = Instant.now();
        lc.stoneCount2 = 0;
        for (int i = 0; i < lc.sc; i++) {
            lc.stoneCount2 += calc(lc.stones[i], lc.blinks2);
        }
        System.out.printf("Runtime %d Blinks: %s\n", lc.blinks2, Duration.between(_i, Instant.now()));

        System.out.println();

        System.out.printf("Stones after %d blinks: %d\n", lc.blinks1, lc.stoneCount1);
        System.out.printf("Stones after %d blinks: %d\n", lc.blinks2, lc.stoneCount2);
    }

    private long calc(long stVal, int blinkDepth) {
        if (blinkDepth < 0) {
            return 0;
        }

        String sval;
        long sum = 0;
        if (stVal < PREPSIZE) {
            if (blinkDepth <= PREPBLINKS) {
                sum += lc.prepCounts[(int) stVal][blinkDepth];
            } else {
//                sum += lc.prepCounts[(int) stVal][PREPBLINKS];
                for (var i = 0; i < lc.prepCounts[(int) stVal][PREPBLINKS]; i++) {
                    sum += calc(lc.prepStones[(int) stVal][i], blinkDepth - PREPBLINKS);
                }
            }
        } else if ((sval = String.valueOf(stVal)).length() % 2 == 0) {
            // siehe https://stackoverflow.com/a/1306751/4576064
            if (blinkDepth > 0) {
                sum += calc(Long.parseLong(sval.substring(0, sval.length() / 2)), blinkDepth - 1);
                sum += calc(Long.parseLong(sval.substring(sval.length() / 2)), blinkDepth - 1);
            } else {
                sum += 1;
            }
        } else {
            if (blinkDepth > 0) {
                sum += calc(stVal * 2024, blinkDepth - 1);
            } else {
                sum += 1;
            }
        }

        return sum;
    }

    private void prepare() {
        for (var i = 0; i < PREPSIZE; i++) {
            lc.prepStones[i][0] = i;
            lc.prepCounts[i][0] = 1;
            for (var bl = 1; bl <= PREPBLINKS; bl++) {
                lc.prepCounts[i][bl] = blink(lc.prepStones[i], (int) lc.prepCounts[i][bl - 1]);
            }
        }
    }

    private void dump(long[] stones, int sc) {
        for (int i = 0; i < sc; i++) {
            System.out.print(stones[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    private int blink(long[] stones, int sc) {

        var newSc = sc;
        long num;
        String sval;

        for (int i = 0; i < sc; i++) {
            num = stones[i];
            if (num == 0) {
                stones[i] = 1L;

            } else if ((sval = String.valueOf(num)).length() % 2 == 0) {
                // siehe https://stackoverflow.com/a/1306751/4576064
                stones[i] = Long.parseLong(sval.substring(0, sval.length() / 2));
                stones[newSc++] = Long.parseLong(sval.substring(sval.length() / 2));
            } else {
                stones[i] = num * 2024;
            }
        }

        return newSc;
    }
}
