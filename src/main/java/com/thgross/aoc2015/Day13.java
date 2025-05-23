package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Day13 extends Application {

    String inputFilename = "aoc2015/input13.txt";

    Pattern patternLine = Pattern.compile("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+).");

    static class Person {
        Map<String, Integer> happiness = new HashMap<>();
    }

    public static void main(String[] args) {
        var app = (new Day13());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        Map<String, Person> persons = new HashMap<>();

        for (String line : lines) {
            var matcher = patternLine.matcher(line);
            while (matcher.find()) {
                var name1 = matcher.group(1);
                var operation = matcher.group(2);
                var happinessString = matcher.group(3);
                var name2 = matcher.group(4);

                if (!persons.containsKey(name1)) {
                    persons.put(name1, new Person());
                }

                var happiness = Objects.equals(operation, "gain") ? Integer.parseInt(happinessString) : -Integer.parseInt(happinessString);

                persons.get(name1).happiness.put(name2, happiness);
            }
        }

        // Part 1
        List<List<String>> permutations = new ArrayList<>();
        generatePermutations(new ArrayList<>(persons.keySet()), new ArrayList<>(), permutations);

        // Calc global Happiness
        int maxHappiness1 = Integer.MIN_VALUE;
        Integer permutations1 = permutations.size();
        for (List<String> permutation : permutations) {
            var happiness = calcHappiness(permutation, persons);
            if (happiness > maxHappiness1) {
                maxHappiness1 = happiness;
            }
        }

        // Part 2
        persons.forEach((pname, p) -> p.happiness.put("Ich", 0));
        Set<String> personnames = persons.keySet();
        var ich = new Person();
        personnames.forEach(n -> ich.happiness.put(n, 0));
        persons.put("Ich", ich);

        permutations = new ArrayList<>();
        generatePermutations(new ArrayList<>(persons.keySet()), new ArrayList<>(), permutations);

        // Calc global Happiness
        int maxHappiness2 = Integer.MIN_VALUE;
        Integer permutations2 = permutations.size();
        for (List<String> permutation : permutations) {
            var happiness = calcHappiness(permutation, persons);
            if (happiness > maxHappiness2) {
                maxHappiness2 = happiness;
            }
        }

        System.out.println("--------------------------------------");
        System.out.printf("Part 1: %d people. %d permutations. Max Happiness: %d\n", persons.size(), permutations1, maxHappiness1);
        System.out.printf("Part 1: %d people. %d permutations. Max Happiness: %d\n", persons.size(), permutations2, maxHappiness2);
//        System.out.printf("Part 2: Sum: %d\n", val2);
    }

    private int calcHappiness(List<String> namelist, Map<String, Person> persons) {
        int happiness = 0;

        for (int i = 0; i < namelist.size(); i++) {

            String name = namelist.get(i);
            String nameLeft = namelist.get((i - 1 + namelist.size()) % namelist.size());
            String nameRight = namelist.get((i + 1) % namelist.size());

            happiness += persons.get(name).happiness.get(nameLeft);
            happiness += persons.get(name).happiness.get(nameRight);
        }

        return happiness;
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
