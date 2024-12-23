package com.thgross;

import java.util.*;

public class AOC2024_19_PatternMatcher {

        public static void main(String[] args) {
            // Beispiel-Listen
            String[] patterns = {"brwrr", "bggr", "gbbr", "rrbgbr", "ubwu", "bwurrg", "brgr", "bbrgwb"};
            String[] towels = {"r", "wr", "b", "g", "bwu", "rb", "gb", "br"};

            // Konvertiere die Listen in char[]-Arrays
            List<char[]> patternChars = convertToCharArrayList(patterns);
            List<char[]> towelChars = convertToCharArrayList(towels);

            // Ergebnis berechnen
            for (char[] pattern : patternChars) {
                boolean canBeConstructed = canConstructPattern(pattern, towelChars, 0, new HashMap<>());
                System.out.println("Pattern: " + new String(pattern) + " -> " + (canBeConstructed ? "kann konstruiert werden" : "kann nicht konstruiert werden"));
            }
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
