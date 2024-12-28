package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day01 extends Application {
    public static void main(String[] args) {
        (new Day01()).run();
    }

    @Override
    public void run() {

        try {
            calcAll("aoc2024/input01.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var lambdaContext = new Object() {
            int lineNumber = 0;
            final int[][] values = new int[2][1000];
            int totalDistance = 0;
            int totalSimilarityScore = 0;
        };

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }
            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                lambdaContext.lineNumber = 0;
                lines.forEach(line -> {
                    // Zeilenweise Verarbeitung
                    var parts = line.split("\\s+");

                    if (parts.length == 2) {
                        lambdaContext.values[0][lambdaContext.lineNumber] = Integer.parseInt(parts[0]);
                        lambdaContext.values[1][lambdaContext.lineNumber] = Integer.parseInt(parts[1]);
                    } else {
                        throw new RuntimeException("konnte Zeile nicht parsen");
                    }

                    lambdaContext.lineNumber++;
                });
            }
        }

        assert lambdaContext.lineNumber == 1000;

        Arrays.sort(lambdaContext.values[0]);
        Arrays.sort(lambdaContext.values[1]);

        // Total Distance berechnen
        for (int l = 0; l < lambdaContext.lineNumber; l++) {
            var distance = Math.abs(lambdaContext.values[0][l] - lambdaContext.values[1][l]);
            lambdaContext.totalDistance += distance;
//            System.out.printf("%4d: %d %d: %d\n", l, lambdaContext.values[0][l], lambdaContext.values[1][l], distance);
        }

        // Similarity Score berechnen
        for (var left = 0; left < lambdaContext.values[0].length; left++) {
            int found = 0;
            for (var right = 0; right < lambdaContext.values[1].length; right++) {
                if (lambdaContext.values[1][right] == lambdaContext.values[0][left]) {
                    found++;
                }
            }

            lambdaContext.totalSimilarityScore += lambdaContext.values[0][left] * found;
        }

        System.out.printf("Total Distance: %d\n", lambdaContext.totalDistance);
        System.out.printf("Similarity Score: %d\n", lambdaContext.totalSimilarityScore);
    }
}