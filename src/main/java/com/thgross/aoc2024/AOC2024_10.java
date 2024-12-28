package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Stream;

public class AOC2024_10 extends Application {
    public static void main(String[] args) {
        (new AOC2024_10()).run();
    }

    static class Pdata {
        int[][] map;
        int w, h;
        int trailheads, trailheadsScore, trailheadsRatings;
    }

    // oben, rechts, unten, links
    private final int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private final AOC2024_10.Pdata lc = new AOC2024_10.Pdata();

    @Override
    public void run() {
        try {
            Instant start = Instant.now();
            calcAll("aoc2024/input10.txt");
            Instant stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(start, stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }

            var slines = new ArrayList<String>();

            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                // Zeilenweise Verarbeitung
                lines.forEach(slines::add);
            }

            lc.h = slines.size();
            lc.w = slines.getFirst().length();
            lc.map = new int[lc.h][lc.w];

            // Map einlesen
            for (int y = 0; y < slines.size(); y++) {
                var line = slines.get(y);

                for (int x = 0; x < line.length(); x++) {
                    lc.map[y][x] = Integer.parseInt(String.valueOf(line.charAt(x)));
                }
            }

            lc.trailheads = 0;
            lc.trailheadsScore = 0;
            lc.trailheadsRatings = 0;
            for (int y = 0; y < lc.map.length; y++) {
                for (int x = 0; x < lc.map[y].length; x++) {
                    if (lc.map[y][x] == 0) {
                        lc.trailheads++;
                        cleanMap(lc.map);
                        // Teil 1
                        lc.trailheadsScore += calcScore(lc.map, y, x, false);
                        cleanMap(lc.map);
                        // Teil 2
                        lc.trailheadsRatings += calcScore(lc.map, y, x, true);
                    }
                }
            }

            System.out.printf("Trailheads: %d\n", lc.trailheads);
            System.out.printf("Trailheads Score: %d\n", lc.trailheadsScore);
            System.out.printf("Trailheads Ratings: %d\n", lc.trailheadsRatings);
        }
    }

    private int calcScore(int[][] map, int y, int x, boolean rating) {

        int score = 0;

        for (int[] dir : dirs) {
            var ny = y + dir[0];
            var nx = x + dir[1];
            if (ny >= 0 && ny < lc.h && nx >= 0 && nx < lc.w && map[ny][nx] == map[y][x] + 1) {
                if (map[ny][nx] == 9) {
                    if(!rating) {
                        map[ny][nx] = 19;    // merken, dass wir hier schon waren
                    }
                    score += 1;
                } else {
                    score += calcScore(map, ny, nx, rating);
                }
            }
        }

        return score;
    }

    private void cleanMap(int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 19) {
                    map[y][x] = 9;
                }
            }
        }
    }
}
