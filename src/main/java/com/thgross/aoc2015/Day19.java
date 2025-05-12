package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Day19 extends Application {

    // Cache für bereits berechnete Teilpfade
    private static Map<String, List<String>> pathCache = new HashMap<>();

    //    String inputFilename = "aoc2015/input19-t1.txt";
    String inputFilename = "aoc2015/input19.txt";

    Pattern patternReplacement = Pattern.compile("^(\\w+) => (\\w+)$");

    public static void main(String[] args) {
        var app = (new Day19());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<Map.Entry<String, String>> rules = new ArrayList<>();
        String medicine = "";

        for (String line : lines) {
            var matcherReplacement = patternReplacement.matcher(line);
            if (matcherReplacement.find()) {
                rules.add(new AbstractMap.SimpleEntry<>(matcherReplacement.group(1), matcherReplacement.group(2)));
            } else {
                if (!line.isEmpty()) {
                    medicine = line;
                }
            }
        }

        System.out.println(rules);
        System.out.println(medicine);

        // Part 1
        HashSet<String> molecules1 = new HashSet<>();
        Pattern pattern;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> rule : rules) {
            int index = medicine.indexOf(rule.getKey());
            while (index >= 0) {
//                System.out.printf("%d (%s => %s): ", index, replacement.getKey(), replacement.getValue());
                sb = new StringBuilder(medicine);
                sb.replace(index, index + rule.getKey().length(), rule.getValue());
//                System.out.println(sb.toString());
                molecules1.add(sb.toString());
                // Calc next Index
                index = medicine.indexOf(rule.getKey(), index + 1);
            }
        }

//        System.out.println(molecules1);
        // Part 2
        var replacementCount = findMinimumTransformations("e", medicine, rules);

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | Distinct molecules after %d replacements: %d\n", 1, molecules1.size());
        System.out.printf("Part 2 | Medicine created from 'e' after %d replacements: %d\n", 1, replacementCount);
    }

    // Lösung laut https://old.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/cy4ctw3/.
    // Funktioniert vermutlich deshalb: "Dass dieser Ansatz für Ihr Problem so gut funktioniert, deutet darauf hin,
    // dass die Regeln sorgfältig entworfen wurden, um ein eindeutiges oder zumindest greedy-optimales Ergebnis zu
    // ermöglichen." (https://claude.ai/chat/ca729be3-7c71-4b71-b4d2-08407757fc68)
    public static Integer findMinimumTransformations(String start, String target, List<Map.Entry<String, String>> rules) {
        var z = 0;
        while (!Objects.equals(target, "e")) {
            for (Map.Entry<String, String> rule : rules) {
                var pos = target.indexOf(rule.getValue());
                if(pos != -1) {
                    z++;
                    target = target.replaceFirst(rule.getValue(), rule.getKey());
                }
            }
        }

        return z;
    }
}
