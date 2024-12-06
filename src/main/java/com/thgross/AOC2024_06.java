package com.thgross;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Stream;

public class AOC2024_06 extends Application {
    public static void main(String[] args) {
        (new AOC2024_06()).run();
    }

    public final static int EMPTY = 0;
    public final static int WALL = 1;
    public final static int OBSTACLE = 2;

    public final static int VISITED_UP = 10;
    public final static int VISITED_RIGHT = 11;
    public final static int VISITED_DOWN = 12;
    public final static int VISITED_LEFT = 13;

    public final static int UP = 0;
    public final static int RIGHT = 1;
    public final static int DOWN = 2;
    public final static int LEFT = 3;

    static class Pdata {
        int[][] map;
        int w;
        int h;
        int pxStart;
        int pyStart;
        int px;
        int py;
        int px2;
        int py2;
        int pDir;
    }

    private final Pdata lc = new Pdata();

    @Override
    public void run() {
        try {
            calcAll("input06.txt");
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

            // einlesen
            for (int y = 0; y < slines.size(); y++) {
                var line = slines.get(y);
                for (int x = 0; x < line.length(); x++) {
                    switch (line.charAt(x)) {
                        case '#':
                            lc.map[y][x] = WALL;
                            break;
                        case '.':
                            lc.map[y][x] = EMPTY;
                            break;
                        case '^':
                            lc.map[y][x] = VISITED_UP;
                            lc.pxStart = x;
                            lc.pyStart = y;

                            break;
                    }
                }
            }

            // Teil 1
            resetMap(lc.map);
            var visitedCount = getSteps(lc);

            // Teil 2
            var loopCount = 0;
            for (int y = 0; y < lc.map.length; y++) {
                for (int x = 0; x < lc.map[y].length; x++) {
                    resetMap(lc.map);
                    if (lc.map[y][x] == EMPTY && (x != lc.pxStart || y != lc.pyStart)) {
                        lc.map[y][x] = OBSTACLE;

                        var steps = getSteps(lc);
                        if (steps == -1) {
                            loopCount++;
                        }
                    }
                }
            }

            System.out.printf("Map: %d * %d\n", lc.w, lc.h);
            System.out.printf("Visited: %d\n", visitedCount);
            System.out.printf("Loop Positions: %d\n", loopCount);
        }
    }

    private int getSteps(Pdata lc) {

        int visitedCount = 1;

        do {
            // next step
            switch (lc.pDir) {
                case UP:
                    lc.px2 = lc.px;
                    lc.py2 = lc.py - 1;
                    break;
                case RIGHT:
                    lc.px2 = lc.px + 1;
                    lc.py2 = lc.py;
                    break;
                case DOWN:
                    lc.px2 = lc.px;
                    lc.py2 = lc.py + 1;
                    break;
                case LEFT:
                    lc.px2 = lc.px - 1;
                    lc.py2 = lc.py;
                    break;
            }

            // Außerhalb? Dann fertig
            if (lc.px2 < 0 || lc.px2 >= lc.w || lc.py2 < 0 || lc.py2 >= lc.h) {
                break;
            }

            // Wand oder Hinderniss? dann drehen
            if (lc.map[lc.py2][lc.px2] == WALL || lc.map[lc.py2][lc.px2] == OBSTACLE) {
                lc.pDir = (lc.pDir + 1) % 4;
                continue;
            }

            // gehen
            var visited = VISITED_UP + lc.pDir;
            if (lc.map[lc.py2][lc.px2] == visited) {
                // Loop!
                return -1;
            } else if (lc.map[lc.py2][lc.px2] == EMPTY) {
                visitedCount++;
                lc.map[lc.py2][lc.px2] = visited;
            } else {
                lc.map[lc.py2][lc.px2] = visited;
            }

            lc.px = lc.px2;
            lc.py = lc.py2;

        } while (true);

        return visitedCount;
    }

    private void resetMap(int[][] map) {

        lc.px = lc.pxStart;
        lc.py = lc.pyStart;
        lc.pDir = 0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == VISITED_UP || map[y][x] == VISITED_RIGHT || map[y][x] == VISITED_DOWN || map[y][x] == VISITED_LEFT) {
                    map[y][x] = EMPTY;
                }
                if (map[y][x] == OBSTACLE) {
                    map[y][x] = EMPTY;
                }
            }
        }

        map[lc.py][lc.px] = VISITED_UP;
    }
}
