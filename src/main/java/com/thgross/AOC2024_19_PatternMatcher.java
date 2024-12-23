package com.thgross;

import java.util.*;

public class AOC2024_19_PatternMatcher {

    public static void main(String[] args) {
        // Beispiel-Listen
        List<String> patterns = Arrays.asList("brwrr", "bggr", "gbbr", "rrbgbr", "ubwu", "bwurrg", "brgr", "bbrgwb");
        List<String> towels = Arrays.asList("r", "wr", "b", "g", "bwu", "rb", "gb", "br");

        // Ergebnis berechnen
        for (String pattern : patterns) {
            boolean canBeConstructed = canConstructPattern(pattern, towels);
            System.out.println("Pattern: " + pattern + " -> " + (canBeConstructed ? "kann konstruiert werden" : "kann nicht konstruiert werden"));
        }
    }

    public static boolean canConstructPattern(String pattern, List<String> towels) {
        // Rückgabe, ob das Pattern konstruiert werden kann
        return canConstructPatternHelper(pattern, towels, new HashMap<>());
    }

    private static boolean canConstructPatternHelper(String pattern, List<String> towels, Map<String, Boolean> memo) {
        // Memoisierung, um Wiederholungen zu vermeiden
        if (memo.containsKey(pattern)) {
            return memo.get(pattern);
        }

        // Wenn das Pattern leer ist, kann es immer konstruiert werden
        if (pattern.isEmpty()) {
            return true;
        }

        // Versuche, das Pattern mit den Tüchern zu konstruieren
        for (String towel : towels) {
            if (pattern.startsWith(towel)) {
                String remaining = pattern.substring(towel.length());
                if (canConstructPatternHelper(remaining, towels, memo)) {
                    memo.put(pattern, true);
                    return true;
                }
            }
        }

        // Wenn keine Konstruktion möglich ist, speichern und zurückgeben
        memo.put(pattern, false);
        return false;
    }
}
