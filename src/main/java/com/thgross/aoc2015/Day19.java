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
//        List<String> solution = findMinimumTransformations("e", medicine, rules);

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | Distinct molecules after %d replacements: %d\n", 1, molecules1.size());
//        System.out.printf("Part 2 | Medicine created from 'e' after %d replacements: %d\n", 1, solution.size() - 1);
    }

    /**
     * Findet die minimale Anzahl von Ersetzungen, um vom Anfangsstring zum Zielstring zu gelangen.
     * @param start Der Anfangsstring
     * @param target Der Zielstring
     * @param rules Die Liste der Ersetzungsregeln
     * @return Liste der Strings nach jeder Ersetzung oder null, wenn keine Lösung gefunden wurde
     */
    public static List<String> findMinimumTransformations(String start, String target, List<Map.Entry<String, String>> rules) {
        // Optimierte Suche mit Caching und Heuristiken
        pathCache.clear();

        // Vorverarbeitung: Sortiere Regeln nach ihrer Wahrscheinlichkeit, zum Ziel zu führen
        // (basierend auf der Länge der Ausgabestrings relativ zur Ziellänge)
        rules.sort((r1, r2) -> {
            // Berechne das Verhältnis von Ausgabetext zu Eingabetext
            double ratio1 = (double) r1.getValue().length() / r1.getKey().length();
            double ratio2 = (double) r2.getValue().length() / r2.getKey().length();

            // Wenn das Ziel länger als der Start ist, bevorzuge Regeln, die mehr erweitern
            if (target.length() > start.length()) {
                return Double.compare(ratio2, ratio1);
            } else {
                return Double.compare(ratio1, ratio2);
            }
        });

        // Bi-direktionaler Ansatz mit Limitierung der Suchtiefe
        return bidirectionalSearch(start, target, rules, 20);
    }

    /**
     * Führt eine bi-direktionale Breitensuche durch, um den kürzesten Pfad zu finden
     */
    private static List<String> bidirectionalSearch(String start, String target, List<Map.Entry<String, String>> rules, int maxDepth) {
        // Vorwärts-Suche (vom Start zum Ziel)
        Queue<List<String>> forwardQueue = new LinkedList<>();
        Map<String, List<String>> forwardVisited = new HashMap<>();

        // Rückwärts-Suche (vom Ziel zum Start) - nur zur Optimierung
        Queue<List<String>> backwardQueue = new LinkedList<>();
        Map<String, List<String>> backwardVisited = new HashMap<>();

        // Startpunkte initialisieren
        List<String> forwardInitial = new ArrayList<>();
        forwardInitial.add(start);
        forwardQueue.add(forwardInitial);
        forwardVisited.put(start, forwardInitial);

        List<String> backwardInitial = new ArrayList<>();
        backwardInitial.add(target);
        backwardQueue.add(backwardInitial);
        backwardVisited.put(target, backwardInitial);

        // Suche bis zur maximalen Tiefe oder bis eine Lösung gefunden wird
        for (int depth = 0; depth < maxDepth; depth++) {
            // Vorwärts-Suche einen Schritt durchführen
            List<String> forwardResult = processLevel(forwardQueue, forwardVisited, rules, target, true, depth);
            if (forwardResult != null) {
                return forwardResult;
            }

            // Prüfen, ob wir eine Verbindung zwischen vorwärts und rückwärts gefunden haben
            for (String forward : forwardVisited.keySet()) {
                if (backwardVisited.containsKey(forward)) {
                    // Verbindung gefunden! Kombiniere die Pfade
                    List<String> forwardPath = forwardVisited.get(forward);
                    List<String> backwardPath = backwardVisited.get(forward);

                    // Kombiniere die Pfade (rückwärts muss umgekehrt werden)
                    List<String> combinedPath = new ArrayList<>(forwardPath);

                    // Cache-Aktualisierung für zukünftige Suchen
                    pathCache.put(start + "->" + forward, new ArrayList<>(forwardPath));
                    pathCache.put(forward + "->" + target, new ArrayList<>(backwardPath));

                    return combinedPath;
                }
            }

            // Rückwärts-Suche einen Schritt durchführen
            // (Nur zur Optimierung der Suche, nicht für die eigentliche Lösung)
            if (depth < maxDepth / 2) { // Beschränke die Rückwärtssuche auf die Hälfte der Tiefe
                processLevel(backwardQueue, backwardVisited, rules, start, false, depth);
            }
        }

        return null; // Keine Lösung gefunden innerhalb der maximalen Tiefe
    }

    /**
     * Verarbeitet einen Level der Breitensuche
     */
    private static List<String> processLevel(Queue<List<String>> queue, Map<String, List<String>> visited,
                                             List<Map.Entry<String, String>> rules, String target, boolean isForward, int depth) {
        // Anzahl der Elemente in der aktuellen Ebene
        int levelSize = queue.size();

        // Verarbeite jeden Knoten in der aktuellen Ebene
        for (int i = 0; i < levelSize; i++) {
            List<String> path = queue.poll();
            String current = path.get(path.size() - 1);

            // Ziel erreicht?
            if (current.equals(target)) {
                return path;
            }

            // Prüfen, ob wir eine Cache-Lösung für dieses Teilproblem haben
            String cacheKey = current + "->" + target;
            if (pathCache.containsKey(cacheKey)) {
                List<String> cachedPath = pathCache.get(cacheKey);
                List<String> completePath = new ArrayList<>(path.subList(0, path.size() - 1));
                completePath.addAll(cachedPath);
                return completePath;
            }

            // Berechne neue Knoten nur, wenn wir nicht zu weit gegangen sind
            if (path.size() <= depth + 1) { // +1 für den Anfangsknoten
                // Generiere alle möglichen nächsten Strings
                generateNextStrings(current, rules, path, queue, visited, target, isForward);
            }
        }

        return null; // Keine Lösung auf dieser Ebene gefunden
    }

    /**
     * Generiert alle möglichen nächsten Strings durch Anwendung der Regeln
     */
    private static void generateNextStrings(String current, List<Map.Entry<String, String>> rules, List<String> path,
                                            Queue<List<String>> queue, Map<String, List<String>> visited,
                                            String target, boolean isForward) {
        // Limit für die Länge des generierten Strings
        int maxLength = Math.max(current.length(), target.length()) * 2;

        for (Map.Entry<String, String> rule : rules) {
            String from = isForward ? rule.getKey() : rule.getValue();
            String to = isForward ? rule.getValue() : rule.getKey();

            // Alle möglichen Positionen für diese Ersetzung finden
            int pos = 0;
            while ((pos = current.indexOf(from, pos)) != -1) {
                // Neue Zeichenkette nach Anwendung der Regel
                String newString = current.substring(0, pos) + to + current.substring(pos + from.length());

                // Wenn wir diesen String noch nicht besucht haben und er nicht zu lang ist
                if (!visited.containsKey(newString) && newString.length() <= maxLength) {
                    // Neuen Pfad erstellen
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(newString);

                    // Zur Queue hinzufügen und als besucht markieren
                    queue.add(newPath);
                    visited.put(newString, newPath);

                    // Aktualisiere den Cache
                    String cacheKey = path.get(0) + "->" + newString;
                    pathCache.put(cacheKey, new ArrayList<>(newPath));
                }

                pos += 1; // Weitersuchen
            }
        }
    }
}
