package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day06 extends Application {

    String inputFilename = "aoc2025/input06.txt";

    protected static class Problem {
        static final char OP_MUL = '*';
        static final char OP_ADD = '+';

        long[] arg;
        char operation;

        Problem(long[] arg, char operation) {
            this.arg = arg;
            this.operation = operation;
        }

        /**
         * Berechnet das Ergebnis basierend auf dem Operator.
         */
        public long calculate() {
            if (arg == null || arg.length == 0) return 0;

            long result = arg[0];
            for (int i = 1; i < arg.length; i++) {
                switch (operation) {
                    case OP_ADD -> result += arg[i];
                    case OP_MUL -> result *= arg[i];
                    default -> throw new UnsupportedOperationException("Unbekannter Operator: " + operation);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            if (arg == null || arg.length == 0) return "Keine Daten";

            String expression = Arrays.stream(arg)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" " + operation + " "));

            try {
                return expression + " = " + calculate();
            } catch (ArithmeticException e) {
                return expression + " = Fehler (" + e.getMessage() + ")";
            }
        }
    }

    public static void main(String[] args) {
        var app = (new Day06());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1Total;
        long part2Total;

        var patternLine = Pattern.compile("(\\s*(\\S+))");
        var matcher = patternLine.matcher(lines.getFirst());
        int problemCount = 0;
        int argCount = lines.size() - 1;    // weil die letzte Zeile die Operatoren enthält
        while (matcher.find()) {
            problemCount++;
        }

        var problems1 = new ArrayList<Problem>(problemCount);
        for (int i = 0; i < problemCount; i++) {
            problems1.add(new Problem(new long[argCount], '\0'));
        }
        int opId = 0;
        for (String line : lines) {

            matcher = patternLine.matcher(line);
            int problemId = 0;

            while (matcher.find()) {
                if (opId < argCount) {
                    problems1.get(problemId).arg[opId] = Integer.parseInt(matcher.group(2));    // Group 1 sind Leerzeichen (optional)
                } else {
                    problems1.get(problemId).operation = matcher.group(2).charAt(0);    // Group 1 sind Leerzeichen (optional)
                }
                problemId++;
            }

            opId++;
        }

        part1Total = problems1.stream()
                .mapToLong(Problem::calculate)
                .sum();

//        problems.forEach(System.out::println);

        // Part 2
        var linesCount = lines.size();
        var problems2 = new ArrayList<Problem>();
        Problem prob;
        List<Long> probVals = new ArrayList<>();
        char probOp = '\0';
        for (int x = 0; x < lines.getFirst().length(); x++) {
            Long probVal = 0L;
            if (lines.get(linesCount - 1).charAt(x) != ' ') {
                // neues Problem
                if (!probVals.isEmpty() && probOp != '\0') {
                    problems2.add(
                            new Problem(
                                    probVals.stream().mapToLong(l -> l).toArray(),
                                    probOp
                            )
                    );
                }
                probVals = new ArrayList<>();
                probVal = 0L;
                probOp = lines.get(linesCount - 1).charAt(x);
            }
            int posmul = 1;
            for (int y = lines.size() - 2; y >= 0; y--) {
                var numchar = lines.get(y).charAt(x);
                if (numchar != ' ') {
                    probVal += (long) (numchar - '0') * posmul;
                    posmul *= 10;
                }
            }
            if (probVal != 0L) {
                probVals.add(probVal);
            }
        }
        if (!probVals.isEmpty() && probOp != '\0') {
            problems2.add(
                    new Problem(
                            probVals.stream().mapToLong(l -> l).toArray(),
                            probOp
                    )
            );
        }

        part2Total = problems2.stream()
                .mapToLong(Problem::calculate)
                .sum();

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Total: %d\n", part1Total);
        System.out.printf("Part 2 Total: %d\n", part2Total);
    }
}
