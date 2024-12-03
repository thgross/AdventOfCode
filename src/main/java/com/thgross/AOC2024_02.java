package com.thgross;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class AOC2024_02 extends Application {
    public static void main(String[] args) {
        (new AOC2024_02()).run();
    }

    public static final int MAXREPORTS = 1000;
    public static final int MAXVALS = 10;

    @Override
    public void run() {
        try {
            calcAll("input02.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var lc = new Object() {
            int lineNumber = 0;
            final int[][] values = new int[MAXREPORTS][MAXVALS];
            int totalReportsSafe = 0;
            int totalReportsSafeWithDampener = 0;
        };

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }
            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                lc.lineNumber = 0;
                lines.forEach(line -> {
                    // Zeilenweise Verarbeitung
                    var parts = line.split("\\s+");

                    for (int pt = 0; pt < MAXVALS; pt++) {
                        if (pt < parts.length) {
                            lc.values[lc.lineNumber][pt] = Integer.parseInt(parts[pt]);
                        } else {
                            lc.values[lc.lineNumber][pt] = 0;
                        }
                    }
                    lc.lineNumber++;
                });
            }
        }

        assert lc.lineNumber == 1000;

        // Teil 1
        for (int report = 0; report < MAXREPORTS; report++) {

            var reportSafe = isReportSafe(lc.values[report], false);

            if (reportSafe) {
                lc.totalReportsSafe++;
            }
        }

        // Teil 2
        for (int report = 0; report < MAXREPORTS; report++) {

            var reportSafe = isReportSafe(lc.values[report], true);

            if (reportSafe) {
                lc.totalReportsSafeWithDampener++;
            }
        }

        System.out.printf("Total Safe: %d\n", lc.totalReportsSafe);
        System.out.printf("Total Safe with Dumpener: %d\n", lc.totalReportsSafeWithDampener);
    }

    private boolean isReportSafe(int[] report, boolean enableDampener) {

        var reportSafe = isReportSafe(report);

        if (!reportSafe && enableDampener) {
            // Varianten erzeugen und testen
            for (int valnr = 0; valnr < report.length; valnr++) {
                var variante = removeElement(report, valnr);

                reportSafe = isReportSafe(variante);
                if (reportSafe) {
                    // Es wurde eine sichere Variante gefunden!
                    break;
                }
            }
        }

        return reportSafe;
    }

    private boolean isReportSafe(int[] report) {

        var reportDirection = Integer.compare(report[1], report[0]);
        var reportSafe = true;

        for (int valnr = 1; valnr < MAXVALS && report[valnr] != 0; valnr++) {
            var currentIncrease = Math.abs(report[valnr] - report[valnr - 1]);
            var currentDirection = Integer.compare(report[valnr], report[valnr - 1]);

            if (currentDirection != reportDirection) {
                reportSafe = false;
                break;
            }

            if (currentIncrease > 3) {
                reportSafe = false;
                break;
            }
        }

        return reportSafe;
    }
}
