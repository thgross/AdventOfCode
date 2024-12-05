package com.thgross;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class AOC2024_05 extends Application {
    public static void main(String[] args) {
        (new AOC2024_05()).run();
    }

    @Override
    public void run() {
        try {
            calcAll("input05.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var lc = new Object() {
            final List<List<Integer>> orders = new ArrayList<>();
            final List<List<Integer>> updates = new ArrayList<>();

            int middleSum = 0;
            int middleSumReordered = 0;
            int correctUpdates = 0;
        };

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }
            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                lines.forEach(line -> {
                    // Zeilenweise Verarbeitung

                    if (line.matches("\\d+\\|\\d+")) {
                        var parts = line.split("\\|");
                        lc.orders.add(Arrays.asList(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                    } else if (line.matches("(\\d+,)+\\d+")) {
                        var parts = line.split(",");
                        var pages = new ArrayList<Integer>();
                        for (String part : parts) {
                            pages.add(Integer.parseInt(part));
                        }

                        lc.updates.add(pages);
                    }
                });
            }
        }

        // Teil 1
        for (List<Integer> update : lc.updates) {
            if (checkUpdate(update, lc.orders)) {
                lc.correctUpdates++;
                lc.middleSum += getMiddleValue(update);
            } else {
                bsort(update, lc.orders);
                lc.middleSumReordered += getMiddleValue(update);
            }
        }

        System.out.printf("# Updates: %d\n", lc.updates.size());
        System.out.printf("# Orders: %d\n", lc.orders.size());
        System.out.printf("# correct Orders: %d\n", lc.correctUpdates);
        System.out.println();
        System.out.printf("Middle Sum (correct order): %d\n", lc.middleSum);
        System.out.printf("Middle Sum (reordered): %d\n", lc.middleSumReordered);
    }

    private boolean checkUpdate(List<Integer> update, List<List<Integer>> orders) {

        for (List<Integer> order : orders) {
            var i1 = update.indexOf(order.get(0));
            var i2 = update.indexOf(order.get(1));

            if (i1 != -1 && i2 != -1 && i1 > i2) {
                return false;
            }
        }

        return true;
    }

    private Integer getMiddleValue(List<Integer> update) {

        return update.get(update.size() / 2);

    }

    private void bsort(List<Integer> update, List<List<Integer>> orders) {

        Integer tmp;

        for (int i = update.size(); i > 1; i--) {
            for (int k = 0; k < i - 1; k++) {

                if (needSwap(update.get(k), update.get(k + 1), orders)) {

                    // Swap
                    tmp = update.get(k);
                    update.set(k, update.get(k+1));
                    update.set(k+1, tmp);
                }
            }
        }
    }

    private boolean needSwap(Integer v1, Integer v2, List<List<Integer>> orders) {
        for (List<Integer> order : orders) {
            if (order.get(0).equals(v2) && order.get(1).equals(v1)) {
                return true;
            }
        }

        return false;
    }
}
