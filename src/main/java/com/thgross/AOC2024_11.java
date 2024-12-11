package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AOC2024_11 extends Application {
    public static void main(String[] args) {
        (new AOC2024_11()).run();
    }

    static class Pdata {
        List<Long> stones = new ArrayList<>();
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
            lc.stones.add(Long.parseLong(s));
        }

        for (int blink = 0; blink < lc.blinks1; blink++) {
            blink(lc.stones);
        }
        lc.stoneCount1 = lc.stones.size();

        for (int blink = 0; blink < lc.blinks2; blink++) {
            var istart = Instant.now();
            blink(lc.stones);
            System.out.printf("Blink %d: %s\n", lc.blinks1 + blink, Duration.between(istart, Instant.now()));
        }
        lc.stoneCount2 = lc.stones.size();

        System.out.printf("Stones after %d blinks: %d\n", lc.blinks1, lc.stoneCount1);
        System.out.printf("Stones after %d blinks: %d\n", lc.blinks1 + lc.blinks2, lc.stoneCount2);
    }

    private void blink(List<Long> stones) {
        int loopMax = stones.size();

        for (int i = 0; i < loopMax; i++) {
            var num = stones.get(i);
            if (num == 0) {
                stones.set(i, 1L);

            } else if (String.valueOf(num).length() % 2 == 0) {
                // siehe https://stackoverflow.com/a/1306751/4576064
                var sval = String.valueOf(num);
                stones.set(i, Long.valueOf(sval.substring(0, sval.length() / 2)));
                stones.add(i + 1, Long.valueOf(sval.substring(sval.length() / 2)));
                i++;
                loopMax++;
            } else {
                stones.set(i, num * 2024);
            }
        }
    }
}
