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
        String medicine = "";

        for (String line : lines) {
            var matcherReplacement = patternReplacement.matcher(line);
            if (matcherReplacement.find()) {
                replacements.add(new AbstractMap.SimpleEntry<>(matcherReplacement.group(1), matcherReplacement.group(2)));
            } else {
                if (!line.isEmpty()) {
                    medicine = line;
                }
            }
        }

        System.out.println(replacements);
        System.out.println(medicine);

        // Part 1
        HashSet<String> molecules1 = new HashSet<>();
        Pattern pattern;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> replacement : replacements) {
            int index = medicine.indexOf(replacement.getKey());
            while (index >= 0) {
//                System.out.printf("%d (%s => %s): ", index, replacement.getKey(), replacement.getValue());
                sb = new StringBuilder(medicine);
                sb.replace(index, index + replacement.getKey().length(), replacement.getValue());
//                System.out.println(sb.toString());
                molecules1.add(sb.toString());
                // Calc next Index
                index = medicine.indexOf(replacement.getKey(), index + 1);
            }
        }

//        System.out.println(molecules1);
        // Part 2
//        List<String> solution = findMinimumTransformations("e", medicine, replacements);

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | Distinct molecules after %d replacements: %d\n", 1, molecules1.size());
//        System.out.printf("Part 2 | Medicine created from 'e' after %d replacements: %d\n", 1, solution.size() - 1);
    }

    /**
     * Findet die minimale Anzahl von Ersetzungen, um vom Anfangsstring zum Zielstring zu gelangen.
     *
     * @param start  Der Anfangsstring
     * @param target Der Zielstring
     * @param rules  Die Liste der Ersetzungsregeln
     * @return Liste der Strings nach jeder Ersetzung oder null, wenn keine Lösung gefunden wurde
     */
    public static List<String> findMinimumTransformations(String start, String target, List<Map.Entry<String, String>> rules) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Startpunkt zur Queue hinzufügen
        List<String> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String current = path.getLast();

            // Ziel erreicht?
            if (current.equals(target)) {
                return path;
            }

            // Alle möglichen Ersetzungen für den aktuellen String ausprobieren
            for (Map.Entry<String, String> rule : rules) {
                String from = rule.getKey();
                String to = rule.getValue();

                // Alle möglichen Positionen für diese Ersetzung finden
                int pos = 0;
                while ((pos = current.indexOf(from, pos)) != -1) {
                    // Neue Zeichenkette nach Anwendung der Regel
                    String newString = current.substring(0, pos) + to + current.substring(pos + from.length());

                    // Wenn wir diesen String noch nicht besucht haben
                    if (!visited.contains(newString) && newString.length() <= target.length() * 2) { // Längengrenze als Heuristik
                        visited.add(newString);

                        // Neuen Pfad erstellen
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(newString);
                        queue.add(newPath);
                    }

                    pos += 1; // Weitersuchen
                }
            }
        }

        // Keine Lösung gefunden
        return null;
    }
}
