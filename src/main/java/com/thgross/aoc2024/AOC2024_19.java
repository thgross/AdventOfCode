package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AOC2024_19 extends Application {

    String inputFilename = "aoc2024/input19.txt";
//    String inputFilename = "input19-t1.txt";

    public static void main(String[] args) {
        var app = (new AOC2024_19());
        app.run(app.inputFilename);
    }

    List<char[]> towels = new ArrayList<>();
    char[][] patterns;

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var inputTowels = lines.getFirst().split(", ");
        for (int i = 0; i < inputTowels.length; i++) {
            towels.add(inputTowels[i].toCharArray());
        }

        List<String> sPatterns = new ArrayList<>();
        for (int lnr = 2; lnr < lines.size(); lnr++) {
            sPatterns.add(lines.get(lnr));
        }
        patterns = new char[sPatterns.size()][];
        for (int i = 0; i < sPatterns.size(); i++) {
            patterns[i] = sPatterns.get(i).toCharArray();
        }

        // Part 1
        int possibleDesigns1 = 0;
        for (int i = 0; i < patterns.length; i++) {
            boolean canBeConstructed = canConstructPattern(patterns[i], towels, 0, new HashMap<>());
            possibleDesigns1 += canBeConstructed ? 1 : 0;
            System.out.println("Pattern: " + new String(patterns[i]) + " -> " + (canBeConstructed ? "kann konstruiert werden" : "kann nicht konstruiert werden"));
        }

        // Part 2
        long possibleDesigns2 = 0;
        for (int i = 0; i < patterns.length; i++) {
            long ways = countWaysToConstructPattern(patterns[i], towels, 0, new HashMap<>());
            possibleDesigns2 += ways;
            System.out.println("Pattern: " + new String(patterns[i]) + " -> " + ways + " Möglichkeit(en)");
        }
        System.out.println("----------------------------------");
        System.out.printf("Part 1 possible designs: %d out of %d\n", possibleDesigns1, patterns.length);
        System.out.printf("Part 2 possible design variants: %d\n", possibleDesigns2);

    }

    private static List<char[]> convertToCharArrayList(String[] strings) {
        List<char[]> charList = new ArrayList<>();
        for (String str : strings) {
            charList.add(str.toCharArray());
        }
        return charList;
    }

    public static boolean canConstructPattern(char[] pattern, List<char[]> towels, int start, Map<Integer, Boolean> memo) {
        if (memo.containsKey(start)) {
            return memo.get(start);
        }

        if (start == pattern.length) {
            return true;
        }

        for (char[] towel : towels) {
            if (matches(pattern, towel, start)) {
                if (canConstructPattern(pattern, towels, start + towel.length, memo)) {
                    memo.put(start, true);
                    return true;
                }
            }
        }

        memo.put(start, false);
        return false;
    }

    public static long countWaysToConstructPattern(char[] pattern, List<char[]> towels, int start, Map<Integer, Long> memo) {
        if (memo.containsKey(start)) {
            return memo.get(start);
        }

        if (start == pattern.length) {
            return 1; // Eine Möglichkeit gefunden
        }

        long totalWays = 0;

        for (char[] towel : towels) {
            if (matches(pattern, towel, start)) {
                totalWays += countWaysToConstructPattern(pattern, towels, start + towel.length, memo);
            }
        }

        memo.put(start, totalWays);
        return totalWays;
    }

    private static boolean matches(char[] pattern, char[] towel, int start) {
        if (start + towel.length > pattern.length) {
            return false;
        }
        for (int i = 0; i < towel.length; i++) {
            if (pattern[start + i] != towel[i]) {
                return false;
            }
        }
        return true;
    }
}
