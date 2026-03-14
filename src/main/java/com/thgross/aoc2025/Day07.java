package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Day07 extends Application {

    String inputFilename = "aoc2025/input07.txt";

    static final char START = 'S';
    static final char EMPTY = '.';
    static final char SPLITTER = '^';
    static final char BEAM = '|';

    static void main() {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1Splits = 0;
        long part2Paths;

        char[][] map = new char[lines.size() / 2][lines.getFirst().length()];
        Pos start = new Pos();

        for (int y = 0; y < lines.size() / 2; y++) {
            var mapline = lines.get(y * 2);
            for (int x = 0; x < mapline.length(); x++) {
                map[y][x] = mapline.charAt(x);
                if (map[y][x] == START) {
                    start.x = x;
                    start.y = y;
                }
            }
        }

        var colorMap = Map.of(
                START, YELLOW_BRIGHT,
                BEAM, YELLOW_BRIGHT,
                EMPTY, BLACK_BRIGHT,
                SPLITTER, BLUE_BRIGHT
        );

//        dumpCharMap(map, colorMap);

        long[][] cmap = new long[lines.size() / 2][lines.getFirst().length()];

        // Part 2 calculations
        countPaths(map, cmap);
        part2Paths = calcCMap(map, cmap, false);

        // Part 1 calculations (modifies map)
        for (int y = 1; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                char cTop = map[y - 1][x];
                if (c == SPLITTER && (cTop == START || cTop == BEAM)) {
                    part1Splits++;
                    // Beam weiter nach unten wandern lassen, links und rechts
                    for (int ly = y; ly < map.length; ly++) {
                        if (map[ly][x - 1] == EMPTY) {
                            map[ly][x - 1] = BEAM;
                        } else {
                            break;
                        }
                    }
                    for (int ry = y; ry < map.length; ry++) {
                        if (map[ry][x + 1] == EMPTY) {
                            map[ry][x + 1] = BEAM;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("------------------------------------");
//        dumpCharMap(map, colorMap);

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Splits: %d\n", part1Splits);
        System.out.printf("Part 2 Paths: %d\n", part2Paths);
    }

    protected void countPaths(char[][] map, long[][] cmap) {
        for (int x = 0; x < map[0].length; x++) {
            if (map[0][x] == START) {
                cmap[0][x] = 1;
            }
        }

        for (int y = 1; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == SPLITTER) {
                    cmap[y][x - 1] += cmap[y - 1][x];
                    cmap[y][x + 1] += cmap[y - 1][x];
                } else {
                    cmap[y][x] += cmap[y - 1][x];
                }
            }
        }
    }

    long calcCMap(char[][] map, long[][] cmap, boolean dumpMap) {
        long lsum = 0;
        for (int y = 0; y < cmap.length; y++) {
            lsum = 0;
            for (int x = 0; x < cmap[y].length; x++) {
                if (map[y][x] == SPLITTER) {
                    if (dumpMap) System.out.printf("%s  ", SPLITTER);
                } else {
                    lsum += cmap[y][x];
                    if (dumpMap) System.out.printf("%-3d", cmap[y][x]);
                }
            }
            if (dumpMap) System.out.printf("\t%3d\n", lsum);
        }

        return lsum;
    }
}
