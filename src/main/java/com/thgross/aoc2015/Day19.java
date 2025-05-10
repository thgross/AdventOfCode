package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Day19 extends Application {

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

        List<Map.Entry<String, String>> replacements = new ArrayList<>();
        String molecule = "";

        for (String line : lines) {
            var matcherReplacement = patternReplacement.matcher(line);
            if (matcherReplacement.find()) {
                replacements.add(new AbstractMap.SimpleEntry<>(matcherReplacement.group(1), matcherReplacement.group(2)));
            } else {
                if (!line.isEmpty()) {
                    molecule = line;
                }
            }
        }

        System.out.println(replacements);
        System.out.println(molecule);

        // Part 1
        HashSet<String> molecules1 = new HashSet<>();
        Pattern pattern;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> replacement : replacements) {
            int index = molecule.indexOf(replacement.getKey());
            while (index >= 0) {
//                System.out.printf("%d (%s => %s): ", index, replacement.getKey(), replacement.getValue());
                sb = new StringBuilder(molecule);
                sb.replace(index, index + replacement.getKey().length(), replacement.getValue());
//                System.out.println(sb.toString());
                molecules1.add(sb.toString());
                // Calc next Index
                index = molecule.indexOf(replacement.getKey(), index + 1);
            }
        }

//        System.out.println(molecules1);

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | Distinct molecules after %d replacements: %d\n", 1, molecules1.size());
//        System.out.printf("Part 2 Lights on after %d steps: %d\n", steps, part2LightsOn);
//        System.out.printf("Part 2 Ways with min Containers: %d\n", part2MinNumContainers.get(lowestContainerNum));
    }
}
