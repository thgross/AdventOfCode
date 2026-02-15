package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Day05 extends Application {

    String inputFilename = "aoc2025/input05.txt";

    protected record Range(long from, long to) {
    }

    public static void main(String[] args) {
        var app = (new Day05());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1Fresh = 0;
        long part2Fresh = 0;

        var rawRanges = new ArrayList<Range>();
        var ingreds = new ArrayList<Long>();

        var patternRange = Pattern.compile("^(\\d+)-(\\d+)$");
        var patternIngredient = Pattern.compile("^(\\d+)$");

        // Import Data
        for (String line : lines) {
            var matchRange = patternRange.matcher(line);
            var matchIngred = patternIngredient.matcher(line);
            if (matchRange.matches()) {
                rawRanges.add(new Range(Long.parseLong(matchRange.group(1)), Long.parseLong(matchRange.group(2))));
            } else if (matchIngred.matches()) {
                ingreds.add(Long.parseLong(matchIngred.group(1)));
            }
        }

        var ranges = mergeRanges(rawRanges);

        System.out.printf("Ranges before merge: %d\n", rawRanges.size());
        System.out.printf("Ranges after merge: %d\n", ranges.size());

        // Part 1 Calculation
        for (Long ingred : ingreds) {
            for (Range range : ranges) {
                if (ingred >= range.from && ingred <= range.to) {
                    part1Fresh++;
                    break;
                }
            }
        }

        for (Range range : ranges) {
            part2Fresh += range.to - range.from + 1;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 fresh: %d\n", part1Fresh);
        System.out.printf("Part 1 fresh: %d\n", part2Fresh);
    }

    protected List<Range> mergeRanges(List<Range> input) {
        if (input.isEmpty()) return List.of();

        // 1. Sortieren nach 'from'
        List<Range> sorted = input.stream()
                .sorted(Comparator.comparingLong(Range::from))
                .toList();

        List<Range> merged = new ArrayList<>();

        // Wir starten mit dem ersten Element
        Range current = sorted.getFirst();

        for (int i = 1; i < sorted.size(); i++) {
            Range next = sorted.get(i);

            // Prüfen, ob Überlappung oder Berührung (next.from <= current.to + 1)
            // Das "+ 1" deckt das "Berühren" ab (z.B. [1,5] und [6,10])
            if (next.from() <= current.to() + 1) {
                // Verschmelzen: Wir nehmen das Maximum der Endpunkte
                current = new Range(current.from(), Math.max(current.to(), next.to()));
            } else {
                // Keine Überlappung: Altes Intervall speichern, neues beginnen
                merged.add(current);
                current = next;
            }
        }

        // Das letzte Element hinzufügen
        merged.add(current);

        return merged;
    }
}
