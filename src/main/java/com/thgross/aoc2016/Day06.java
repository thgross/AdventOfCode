package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day06 extends Application {

    String inputFilename = "aoc2016/input06.txt";

    public static void main(String[] args) {
        var app = (new Day06());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        List<char[]> messages = new ArrayList<>();
        for (String line : lines) {
            messages.add(line.toCharArray());
        }

        List<Map<Character, Integer>> charCountMaps = Stream
                .generate(() -> new HashMap<Character, Integer>()) // Erzeugt einen Stream von neuen, leeren HashMaps
                .limit(lines.getFirst().length()) // Begrenzt die Anzahl der Maps auf die gewünschte Größe
                .collect(Collectors.toList()); // Sammelt die Maps in einer List

        for (char[] messageCharArray : messages) {
            for (int i = 0; i < messageCharArray.length; i++) {
                var charAtI = messageCharArray[i];
                var charCountsAtI = charCountMaps.get(i);
                if (charCountsAtI.containsKey(charAtI)) {
                    charCountsAtI.put(charAtI, charCountsAtI.get(charAtI) + 1);
                } else {
                    charCountsAtI.put(charAtI, 1);
                }
            }
        }

        StringBuilder p1Message = new StringBuilder();
        StringBuilder p2Message = new StringBuilder();

        List<Map.Entry<Character, Integer>> charlist;

        for (var charCountMap : charCountMaps) {
            charlist = new ArrayList<>(charCountMap.entrySet());
            charlist.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return Integer.signum(o2.getValue() - o1.getValue());
                } else {
                    return Integer.signum(o1.getKey() - o2.getKey());
                }
            });
            p1Message.append(charlist.getFirst().getKey());
            p2Message.append(charlist.getLast().getKey());
        }

        System.out.printf("Part 1 Message: %s\n", p1Message);
        System.out.printf("Part 2 Message: %s\n", p2Message);
    }
}