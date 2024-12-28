package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AOC2024_13 extends Application {
    public static void main(String[] args) {
        (new AOC2024_13()).run();
    }

    private static class Pos {
        long x, y;

        public Pos(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Machine {
        Pos A;
        Pos B;
        Pos price;

        public Machine(long ax, long ay, long bx, long by, long priceX, long priceY) {
            A = new Pos(ax, ay);
            B = new Pos(bx, by);
            this.price = new Pos(priceX, priceY);
        }
    }

    static class Pdata {
        List<Machine> machines = new ArrayList<>();
        long costA = 3;
        long costB = 1;
        Instant start;
        Instant stop;
    }

    private final AOC2024_13.Pdata lc = new AOC2024_13.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("aoc2024/input13.txt");
            lc.stop = Instant.now();

            System.out.println("======================================");
            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var content = getFileContent(inputFile);

        var pattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)$\\nButton B: X\\+(\\d+), Y\\+(\\d+$)\\nPrize: X=(\\d+), Y=(\\d+)$", Pattern.DOTALL | Pattern.MULTILINE);

        var matcher = pattern.matcher(content);

        while (matcher.find()) {
            lc.machines.add(new Machine(
                    Long.parseLong(matcher.group(1)),
                    Long.parseLong(matcher.group(2)),
                    Long.parseLong(matcher.group(3)),
                    Long.parseLong(matcher.group(4)),
                    Long.parseLong(matcher.group(5)),
                    Long.parseLong(matcher.group(6))
            ));
        }

        // Teil 1
        var machineNr = 0;
        long tokens = 0;
        for (Machine machine : lc.machines) {
            machineNr++;
            var solution = solveLinearEquations(machine.A, machine.B, machine.price);
            var solutionTokens = solution == null ? 0 : solution[0] * lc.costA + solution[1] * lc.costB;
            System.out.printf("machine %2d: %s | tokens: %d\n", machineNr, Arrays.toString(solution), solutionTokens);
            tokens += solutionTokens;
        }

        // Teil 2
        machineNr = 0;
        long tokens2 = 0;
        for (Machine machine : lc.machines) {
            machineNr++;
            machine.price.x += 10000000000000L;
            machine.price.y += 10000000000000L;
            var solution = solveLinearEquations(machine.A, machine.B, machine.price);
            var solutionTokens = solution == null ? 0 : solution[0] * lc.costA + solution[1] * lc.costB;
            System.out.printf("machine %2d: %s | tokens: %d\n", machineNr, Arrays.toString(solution), solutionTokens);
            tokens2 += solutionTokens;
        }

        System.out.println("--------------------------------------");
        System.out.printf("Part 1 tokens: %d\n", tokens);
        System.out.printf("Part 2 tokens: %d\n", tokens2);
    }

    /**
     * Löst ein 2x2 lineares Gleichungssystem mit der Methode von Cramer.
     *
     * @return Array der Lösungen [A, B] oder null, falls keine eindeutige Lösung existiert.
     */
    private long[] solveLinearEquations(Pos A, Pos B, Pos price) {

        // Koeffizientenmatrix
        long a1 = A.x;
        long a2 = A.y;
        long b1 = B.x;
        long b2 = B.y;

        // Determinante der Koeffizientenmatrix
        long determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // Kein eindeutiges Ergebnis
            return null;
        }

        // Cramer's Regel: Berechnung der Determinanten für A und B
        long determinantA = price.x * b2 - price.y * b1;
        long determinantB = a1 * price.y - a2 * price.x;

        // Prüfen, ob die Determinanten ganzzahlig teilbar sind
        if (determinantA % determinant != 0 || determinantB % determinant != 0) {
            return null; // Keine ganzzahlige Lösung
        }

        // Lösungen (nur ganzzahlig)
        long solutionA = determinantA / determinant;
        long solutionB = determinantB / determinant;

        return new long[]{solutionA, solutionB};
    }
}
