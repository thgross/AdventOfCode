package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Day06 extends Application {

    String inputFilename = "aoc2015/input06.txt";

    public static void main(String[] args) {
        var app = (new Day06());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int lights1 = 0;
        long lights2 = 0;

        var map = new int[1000][1000];

        var p1 = Pattern.compile("(turn off|turn on|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");

        for (String line : lines) {
            var matcher = p1.matcher(line);
            if (matcher.find()) {
                var command = matcher.group(1);
                var p1x = Integer.parseInt(matcher.group(2));
                var p1y = Integer.parseInt(matcher.group(3));
                var p2x = Integer.parseInt(matcher.group(4));
                var p2y = Integer.parseInt(matcher.group(5));

                switch (command) {
                    case "turn off":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                map[y][x] = 0;
                            }
                        }
                        break;
                    case "turn on":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                map[y][x] = 1;
                            }
                        }
                        break;
                    case "toggle":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                map[y][x] = 1 - map[y][x];
                            }
                        }
                        break;
                }
            } else {
                throw new RuntimeException("Line could not be parsed: " + line);
            }
        }

        // Part 1 count
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                lights1 += map[y][x];
            }
        }

        // clear map for part 2
        for (int y = 0; y < 1000; y++) {
            Arrays.fill(map[y], 0);
        }

        for (String line : lines) {
            var matcher = p1.matcher(line);
            if (matcher.find()) {
                var command = matcher.group(1);
                var p1x = Integer.parseInt(matcher.group(2));
                var p1y = Integer.parseInt(matcher.group(3));
                var p2x = Integer.parseInt(matcher.group(4));
                var p2y = Integer.parseInt(matcher.group(5));

                switch (command) {
                    case "turn off":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                if (map[y][x] > 0) {
                                    map[y][x]--;
                                }
                            }
                        }
                        break;
                    case "turn on":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                map[y][x]++;
                            }
                        }
                        break;
                    case "toggle":
                        for (int y = p1y; y <= p2y; y++) {
                            for (int x = p1x; x <= p2x; x++) {
                                map[y][x] += 2;
                            }
                        }
                        break;
                }
            } else {
                throw new RuntimeException("Line could not be parsed: " + line);
            }
        }

        // Part 1 count
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                lights2 += map[y][x];
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: lights on: %d\n", lights1);
        System.out.printf("Part 2: lights combined value: %d\n", lights2);
    }
}
