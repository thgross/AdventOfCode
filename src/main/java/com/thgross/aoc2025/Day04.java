package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.List;

public class Day04 extends Application {

    public static final char EMPTY = '.';
    public static final char ROLL = '@';
    public static final char ACCESSIBLE = 'x';
    String inputFilename = "aoc2025/input04.txt";

    public static void main(String[] args) {
        var app = (new Day04());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int part1Rolls = 0;
        int part2Rolls = 0;

        var map = new char[lines.size()][lines.getFirst().length()];

        for (int row = 0; row < lines.size(); row++) {
            var rowchars = lines.get(row).toCharArray();
            // arraycopy anstatt zeichenweise Füllung des Arrays
            System.arraycopy(rowchars, 0, map[row], 0, rowchars.length);
        }

        int accessible = markAccessible(map);
        removeAccessible(map);
        dumpMap(map);

        // Part 1
        part1Rolls += accessible;
        part2Rolls += accessible;

        int removed;
        do {
            markAccessible(map);
            removed = removeAccessible(map);
            part2Rolls += removed;
        } while (removed > 0);

        System.out.println("------------------------------------");
        System.out.printf("Part 1 rolls: %d\n", part1Rolls);
        System.out.printf("Part 2 rolls: %d\n", part2Rolls);
    }

    private int markAccessible(char[] @NonNull [] map) {
        int rolls = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (isAccessible(map, row, col)) {
                    rolls++;
                    map[row][col] = ACCESSIBLE;
                }
            }
        }
        return rolls;
    }

    private int removeAccessible(char[] @NonNull [] map) {
        int removed = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == ACCESSIBLE) {
                    removed++;
                    map[row][col] = EMPTY;
                }
            }
        }
        return removed;
    }

    private boolean isAccessible(char[] @NonNull [] map, int row, int col) {

        if (map[row][col] != ROLL) {
            return false;
        }

        var blockedFields = 0;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int py = row + dy, px = col + dx;
                if (py == row && px == col) {
                    continue;
                }
                if (py >= 0 && py < map.length && px >= 0 && px < map[0].length) {
                    if (map[py][px] != EMPTY) {
                        blockedFields++;
                    }
                }
            }
        }

        return blockedFields < 4;
    }


    private void dumpMap(char[][] map) {
        for (char[] chars : map) {
            for (char aChar : chars) {
                printChar(aChar,
                        switch (aChar) {
                            case EMPTY -> GREEN;
                            case ROLL -> BLUE;
                            case ACCESSIBLE -> RED;
                            default -> throw new RuntimeException("unknown map tile " + aChar);
                        });
            }
            System.out.println();
        }
    }
}
