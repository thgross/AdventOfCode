package com.thgross.aoc2015;

import com.google.common.collect.Sets;
import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;

public class Day17 extends Application {

//        String inputFilename = "aoc2015/input17-t1.txt";
//    int litersStored = 25;
    String inputFilename = "aoc2015/input17.txt";
    int litersStored = 150;

    public static void main(String[] args) {
        var app = (new Day17());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<Integer> containers = new ArrayList<>();
        Set<Integer> containerIDs = new HashSet<>();

        var lineNr = 0;
        for (String line : lines) {
            containers.add(Integer.valueOf(line));
            containerIDs.add(lineNr);
            lineNr++;
        }

        System.out.println(containers);

        // Part 1, Part 2
        int part1Combinations = 0;
        var part2MinNumContainers = new HashMap<Integer, Integer>();
        int iterations = 0;
        var powerSet = Sets.powerSet(containerIDs);

        for (Set<Integer> set : powerSet) {
            var liters = 0;
            iterations++;

            var it = set.iterator();
            while (it.hasNext()) {
                liters += containers.get(it.next());
            }

            if (liters == litersStored) {
                part1Combinations++;
                if (!part2MinNumContainers.containsKey(set.size())) {
                    part2MinNumContainers.put(set.size(), 0);
                }
                part2MinNumContainers.put(set.size(), part2MinNumContainers.get(set.size()) + 1);
            }
        }

        System.out.println(part2MinNumContainers);

        var lowestContainerNum = Collections.min(part2MinNumContainers.keySet());

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 Iterations: %d Combinations: %d\n", iterations, part1Combinations);
        System.out.printf("Part 2 Ways with min Containers: %d\n", part2MinNumContainers.get(lowestContainerNum));
//        System.out.printf("Part 2 Sue-Nr: %d\n", part2SueNr);
    }
}
