package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Application {

    String inputFilename = "input23-t1.txt";

    public static void main(String[] args) {
        var app = (new Day23());
        app.run(app.inputFilename);
    }

    Map<String, Set<String>> comps = new HashMap<>();

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        for (String line : lines) {
            var computers = line.split("-");
            if (!comps.containsKey(computers[0])) {
                comps.put(computers[0], new HashSet<>());
            }
            comps.get(computers[0]).add(computers[1]);
            if (!comps.containsKey(computers[1])) {
                comps.put(computers[1], new HashSet<>());
            }
            comps.get(computers[1]).add(computers[0]);
        }

        List<Set<String>> triplets = new ArrayList<>();
        // find triplets
        for (String c1 : comps.keySet()) {
            for (String c2 : comps.get(c1)) {
                for (String c3 : comps.keySet()) {
                    if (!Objects.equals(c3, c1) && !Objects.equals(c3, c2)) {
                        if (comps.get(c3).contains(c1) && comps.get(c3).contains(c2)) {
                            // Triplet c1, c2, c3
                            HashSet<String> triplet = new HashSet<>();
                            triplet.add(c1);
                            triplet.add(c2);
                            triplet.add(c3);

                            // add to list only if it doesnt already exist
                            if (!triplets.contains(triplet)) {
                                triplets.add(triplet);
                            }
                        }
                    }
                }
            }
        }

        triplets = sortTriplets(triplets);

        var filteredTriplets = triplets.stream()
                .filter(set -> {
                            for (String s : set) {
                                if (s.startsWith("t")) {
                                    return true;
                                }
                            }

                            return false;
                        }
                )
                .toList();

        System.out.println("Computers:");
        for (String s : comps.keySet()) {
            System.out.printf("%s: ", s);
            System.out.println(comps.get(s));
        }

//        System.out.println("all Triplets:");
//        for (Set<String> set : triplets) {
//            System.out.println(set);
//        }

        System.out.println("filtered Triplets:");
        for (Set<String> set : filteredTriplets) {
            System.out.println(set);
        }

        var tStartingTriplets = filteredTriplets.size();

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Triplets: %d\n", tStartingTriplets);
    }

    // Funktion zum Sortieren der Sets und der Liste
    public static List<Set<String>> sortTriplets(List<Set<String>> triplets) {
        // Sortiere jedes Set innerhalb der Liste
        List<Set<String>> sortedTriplets = new ArrayList<>();
        for (Set<String> set : triplets) {
            sortedTriplets.add(new TreeSet<>(set)); // TreeSet sortiert die Elemente
        }

        // Sortiere die Liste der Sets basierend auf ihrem Inhalt
        sortedTriplets.sort((set1, set2) -> {
            Iterator<String> iter1 = set1.iterator();
            Iterator<String> iter2 = set2.iterator();

            while (iter1.hasNext() && iter2.hasNext()) {
                int comparison = iter1.next().compareTo(iter2.next());
                if (comparison != 0) {
                    return comparison; // Unterschied gefunden
                }
            }

            // Wenn beide Sets gleich weit kommen, entscheidet die Größe
            return Integer.compare(set1.size(), set2.size());
        });

        return sortedTriplets;
    }
}
