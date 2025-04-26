package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Day14 extends Application {

    String inputFilename = "aoc2015/input14.txt";

    Pattern patternLine = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");

    static class Reindeer {
        int speed;
        int flyDuration;
        int restDuration;
        int cycleDistance;
        int cycleDuration;
    }

    public static void main(String[] args) {
        var app = (new Day14());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        Map<String, Reindeer> deers = new HashMap<>();

        for (String line : lines) {
            var matcher = patternLine.matcher(line);
            while (matcher.find()) {
                var name = matcher.group(1);
                var speed = matcher.group(2);
                var flyDuration = matcher.group(3);
                var restDuration = matcher.group(4);

                var reindeer = new Reindeer();
                reindeer.speed = Integer.parseInt(speed);
                reindeer.flyDuration = Integer.parseInt(flyDuration);
                reindeer.restDuration = Integer.parseInt(restDuration);
                reindeer.cycleDistance = reindeer.speed * reindeer.flyDuration;
                reindeer.cycleDuration = reindeer.flyDuration + reindeer.restDuration;
                deers.put(name, reindeer);
            }
        }

        // Part 1
        String fastestDeer1 = "?";
        int seconds1 = 2503;
        int distance1 = 0;

        for (String deerName : deers.keySet()) {
            var deerDistance = caldDistance(deers.get(deerName), seconds1);
            if (deerDistance > distance1) {
                distance1 = deerDistance;
                fastestDeer1 = deerName;
            }
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1: %d reindeers. After %d seconds, %s has traveled %d km.\n", deers.size(), seconds1, fastestDeer1, distance1);
    }

    private int caldDistance(Reindeer deer, int seconds) {
        int distance = 0;

        int numCycles = seconds / deer.cycleDuration;
        distance += numCycles * deer.cycleDistance;

        int remainderDuration = seconds % deer.cycleDuration;
        if (remainderDuration <= deer.flyDuration) {
            distance += deer.speed * remainderDuration;
        } else {
            distance += deer.speed * deer.flyDuration;
        }

        return distance;
    }
}
