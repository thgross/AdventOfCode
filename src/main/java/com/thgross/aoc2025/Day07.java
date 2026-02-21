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

    public static void main(String[] args) {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1Splits = 0;
//        long part2Total = 0;

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

        dumpCharMap(map, colorMap);

        for (int y = 1; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                char cTop = map[y - 1][x];
                if (c == SPLITTER && (cTop == START || cTop == BEAM)) {
                    part1Splits++;
                    // Beam weiter nach unten wandern lassen, links und rechts
                    for (int ly = y; ly < map.length; ly++) {
                        if(map[ly][x - 1] == EMPTY) {
                            map[ly][x - 1] = BEAM;
                        } else {
                            break;
                        }
                    }
                    for (int ry = y; ry < map.length; ry++) {
                        if(map[ry][x + 1] == EMPTY) {
                            map[ry][x + 1] = BEAM;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        dumpCharMap(map, colorMap);

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Splits: %d\n", part1Splits);
//        System.out.printf("Part 2 Total: %d\n", part2Total);
    }
}
