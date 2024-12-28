package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AOC2024_07 extends Application {
    public static void main(String[] args) {
        (new AOC2024_07()).run();
    }

    public final static int ADD = 0;
    public final static int MUL = 1;
    public final static int CONCAT = 2;

    static class Pdata {
        List<List<Long>> eq;    // Equations
    }

    private final Pdata lc = new Pdata();

    @Override
    public void run() {
        try {
            Instant start = Instant.now();
            calcAll("aoc2024/input07.txt");
            Instant stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(start, stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        lc.eq = new ArrayList<>();

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }

            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                lines.forEach((line) -> {
                    var parts = line.split(" ");
                    var equation = new ArrayList<Long>();
                    for (int i = 0; i < parts.length; i++) {
                        if (i == 0) {
                            equation.add(Long.valueOf(trim(parts[0], ":")));
                        } else {
                            if (i > 1) {
                                equation.add(0L);
                            }
                            equation.add(Long.valueOf(parts[i]));
                        }
                    }
                    lc.eq.add(equation);
                });
            }

            // Teil 1
            var sum = 0L;
            for (int i = 0; i < lc.eq.size(); i++) {
                var itCount = iterationCount(lc.eq.get(i), 2);

                for (var it = 0; it < itCount; it++) {
                    // Build Iteration
                    buildIteration(lc.eq.get(i), it, 2);
                    var eqsum = calcEq(lc.eq.get(i));
                    if (eqsum.equals(lc.eq.get(i).getFirst())) {
                        sum += eqsum;
                        break;
                    }
                }
            }

            // Teil 2
            var sum2 = 0L;
            for (int i = 0; i < lc.eq.size(); i++) {
                var itCount = iterationCount(lc.eq.get(i), 3);

                for (var it = 0; it < itCount; it++) {
                    // Build Iteration
                    buildIteration(lc.eq.get(i), it, 3);
                    var eqsum = calcEq(lc.eq.get(i));
                    if (eqsum.equals(lc.eq.get(i).getFirst())) {
                        sum2 += eqsum;
                        break;
                    }
                }
            }

            System.out.printf("Sum: %d\n", sum);
            System.out.printf("Sum2: %d\n", sum2);
        }
    }

    void buildIteration(List<Long> eq, int it, int variations) {
        var pos = 0;
        for (int i = 2; i < eq.size(); i += 2) {
            int vval = ((int) (it / Math.pow(variations, pos))) % variations;
            eq.set(i, (long) vval);
            pos++;
        }
    }

    Long calcEq(List<Long> eq) {

        var sum = eq.get(1);

        for (int i = 2; i < eq.size(); i += 2) {
            switch (eq.get(i).intValue()) {    // Add
                case ADD:
                    sum += eq.get(i + 1);
                    break;
                case MUL:
                    sum *= eq.get(i + 1);
                    break;
                case CONCAT:
                    sum = Long.valueOf(sum.toString() + eq.get(i + 1).toString());
                    break;
            }
        }

        return sum;
    }

    int iterationCount(List<Long> eq, int variations) {
        return (int) Math.pow(variations, ((double) (eq.size() - 2) / 2));
    }
}
