package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Application {

    String inputFilename = "aoc2015/input09.txt";

    public static void main(String[] args) {
        var app = (new Day09());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var pDistances = Pattern.compile("^(\\w+) to (\\w+) = (\\d+)$");

        var distances = new HashMap<String, HashMap<String, Integer>>();
        var lnr = 0;
        for (String line : lines) {
            lnr++;
            var matcher = pDistances.matcher(line);
            if (matcher.find()) {
                var city1 = matcher.group(1);
                var city2 = matcher.group(2);
                var distance = Integer.parseInt(matcher.group(3));

                HashMap<String, Integer> useMap;
                if (distances.containsKey(city1)) {
                    useMap = distances.get(city1);
                } else {
                    useMap = new HashMap<String, Integer>();
                    distances.put(city1, useMap);
                }
                useMap.put(city2, distance);

                if (distances.containsKey(city2)) {
                    useMap = distances.get(city2);
                } else {
                    useMap = new HashMap<String, Integer>();
                    distances.put(city2, useMap);
                }
                useMap.put(city1, distance);
            } else {
                throw new RuntimeException("Line not matched: " + lnr);
            }
        }

        List<List<String>> permutations = new ArrayList<>();
        generatePermutations(new ArrayList<String>(distances.keySet()), new ArrayList<>(), permutations);

        var perDistanceShortest = Integer.MAX_VALUE;
        var perDistanceLongest = Integer.MIN_VALUE;
        List<String> shortestPerm = null;
        List<String> longestPerm = null;
        for (List<String> permutation : permutations) {
//            System.out.print(permutation);
            var perDistance = 0;
            for (int i = 0; i < permutation.size() - 1; i++) {
                perDistance += distances.get(permutation.get(i)).get(permutation.get(i + 1));
            }
//            System.out.println(perDistance);
            if(perDistance < perDistanceShortest) {
                perDistanceShortest = perDistance;
                shortestPerm = permutation;
            }
            if(perDistance > perDistanceLongest) {
                perDistanceLongest = perDistance;
                longestPerm = permutation;
            }
        }

//        System.out.println(distances);

        System.out.println("------------------------------------");
        System.out.println(shortestPerm);
        System.out.printf("Part 1: Shortest Distance: %d\n", perDistanceShortest);
        System.out.println(longestPerm);
        System.out.printf("Part 2: Longest Distance: %d\n", perDistanceLongest);
//        System.out.printf("Part 2: charsDoubleCode: %d charsCode: %d | charsDoubleCode - charsCode: %d\n", charsDoubleCode, charsCode, charsDoubleCode - charsCode);
    }

    private static void generatePermutations(List<String> remaining, List<String> current, List<List<String>> result) {
        if (remaining.isEmpty()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < remaining.size(); i++) {
            List<String> newRemaining = new ArrayList<>(remaining);
            List<String> newCurrent = new ArrayList<>(current);
            newCurrent.add(newRemaining.remove(i));
            generatePermutations(newRemaining, newCurrent, result);
        }
    }
}
