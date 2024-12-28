package com.thgross.aoc2024;

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
            calcAll("aoc2024/input02.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean dump = true;

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
        dump = false;
        for (int report = 0; report < MAXREPORTS; report++) {

            var reportSafe = isReportSafe(lc.values[report], report, false);

            if (reportSafe) {
                lc.totalReportsSafe++;
            }
        }
        dump = true;

        // Teil 2
        for (int report = 0; report < MAXREPORTS; report++) {

            var reportSafe = isReportSafe(lc.values[report], report, true);
            if (dump) {
                System.out.println();
            }

            if (reportSafe) {
                lc.totalReportsSafeWithDampener++;
            }
        }

        System.out.printf("Total Safe: %d\n", lc.totalReportsSafe);
        System.out.printf("Total Safe with Dumpener: %d\n", lc.totalReportsSafeWithDampener);
    }

    private void dumpReport(int[] report, int reportNr, boolean safe, int variantnr, int unsafeVal) {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_WHITE = "\u001B[37m";

        final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
        final String ANSI_RED_BACKGROUND = "\u001B[41m";
        final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
        final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
        final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

        System.out.printf("%4d: ", reportNr);
        if (safe) {
            if (unsafeVal > -1) {
                System.out.print("  " + ANSI_BLUE_BACKGROUND + ANSI_BLACK + "  SAFE  " + ANSI_RESET);
            } else {
                System.out.print("  " + ANSI_GREEN_BACKGROUND + ANSI_BLACK + "  SAFE  " + ANSI_RESET);
            }
        } else {
            System.out.print("  " + ANSI_RED_BACKGROUND + ANSI_BLACK + " UNSAFE " + ANSI_RESET);
        }
/*
        if (variantnr == -1) {
            System.out.print(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " ORIG " + ANSI_RESET);
        } else {
            System.out.print(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " V" + (variantnr + 1) + " " + ANSI_RESET);
        }
*/

        for (int i = 0; i < report.length; i++) {
            if (report[i] != 0) {
                if (i == unsafeVal) {
                    System.out.print(ANSI_RED);
                }
                System.out.printf(" %2d", report[i]);
                if (i == unsafeVal) {
                    System.out.print(ANSI_RESET);
                }
            } else {
                System.out.print("   ");
            }
        }
    }

    private boolean isReportSafe(int[] report, int reportNr, boolean enableDampener) {

        var originalSafe = isReportSafe(report);
        var varianteSafe = false;

        if (!originalSafe && enableDampener) {
            // Varianten erzeugen und testen
            for (int valnr = 0; valnr < report.length; valnr++) {
                var variante = removeElement(report, valnr);

                varianteSafe = isReportSafe(variante);
                if (varianteSafe) {
                    if (dump) {
                        dumpReport(report, reportNr, true, -1, valnr);
//                        dumpReport(variante, varianteSafe, valnr, -1);
                    }
                    // Es wurde eine sichere Variante gefunden!
                    originalSafe = varianteSafe;
                    break;
                }
            }
        }

        if (!varianteSafe && dump) {
            dumpReport(report, reportNr, originalSafe, -1, -1);
        }

        return originalSafe;
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
