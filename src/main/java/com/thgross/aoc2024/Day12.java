package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Day12 extends Application {
    public static void main(String[] args) {
        (new Day12()).run();
    }

    private static final int BT_TOP = 0;
    private static final int BT_BOTTOM = 4;
    private static final int BT_LEFT = 2;
    private static final int BT_RIGHT = 3;

    /*
     * d1 and d2 hold the value of one of the 2 dimensions: x or y.
     * For vertical borders, d1 is filled with the x-value of the borders position and d2 ist filled with the
     * y-value of the borders position. This is because for vertical lines (borders), the horizontal (x) distance
     * between borders is more important. When two vertical borders have different x-values, they can never be on
     * the same line.
     * For horizontal borders, the y-value is the most significant.
     * The 'equals' and 'compareTo'-Methods compare the most significant value (d1) first.
     */
    private static class Border implements Comparable<Border> {
        int d1; // most significant Dimension
        int d2; // least significant Dimension
        int type;

        public Border(int d1, int d2, int type) {
            this.d1 = d1;
            this.d2 = d2;
            this.type = type;
        }

        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            Border border = (Border) o;
            return d1 == border.d1 && d2 == border.d2 && type == border.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(d1, d2, type);
        }

        @Override
        public int compareTo(Border b2) {
            if (d1 < b2.d1) {
                return -1;
            }
            if (d1 > b2.d1) {
                return 1;
            }
            if (type < b2.type) {
                return -1;
            }
            if (type > b2.type) {
                return 1;
            }
            // ab hier ist y gleich!
            return Integer.compare(d2, b2.d2);
        }
    }

    private static class Cost {
        public int area;
        public int perimeter;

        public Cost(int area, int perimeter) {
            this.area = area;
            this.perimeter = perimeter;
        }

        public void add(Cost addCost) {
            this.area += addCost.area;
            this.perimeter += addCost.perimeter;
        }
    }

    static class Pdata {
        char[][] map;
        int[] state;
        int emptyPos;
        int w, h;
        Instant start;
        Instant stop;
    }

    private final Day12.Pdata lc = new Day12.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("aoc2024/input12.txt");
            lc.stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        List<String> slines;

        try (var inputStream = getFileAsInputStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }

            slines = new ArrayList<>();

            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                // Zeilenweise Verarbeitung
                lines.forEach(slines::add);
            }
        }

        lc.h = slines.size();
        lc.w = slines.getFirst().length();
        lc.map = new char[lc.h][lc.w];
        lc.state = new int[lc.h * lc.w];
        Arrays.fill(lc.state, 0);

        lc.emptyPos = 0;

        // Map einlesen
        for (int y = 0; y < slines.size(); y++) {
            var line = slines.get(y);
            line.getChars(0, line.length(), lc.map[y], 0);
        }

        var price1 = 0;
        var price2 = 0;
        var areaId = 0;
        while (lc.emptyPos < lc.state.length) {
            int y = lc.emptyPos / lc.w;
            int x = lc.emptyPos % lc.w;
            var borderList = new TreeSet<Border>();
            var cost = floodfill(y, x, lc.map[y][x], borderList, ++areaId);
            var sides = calcSides(borderList);
            price1 += cost.area * cost.perimeter;
            price2 += cost.area * sides;
            System.out.printf("%s: %3d t / %3d b / %4d s | P1: %5d | P2: %5d\n", lc.map[y][x], cost.area, cost.perimeter, sides, cost.area * cost.perimeter, cost.area * sides);
            lc.emptyPos = findEmptyPos(lc.emptyPos);
        }
        System.out.println();

        dumpMap(lc);

        System.out.printf("Part 1 Price: %d\n", price1);
        System.out.printf("Part 2 Price: %d\n", price2);
    }

    private int calcSides(TreeSet<Border> borders) {

        int sides = 0;
        // horizontale Ränder
        var sortedBorders = borders.stream().sorted();
        Iterator<Border> iterator = sortedBorders.iterator();
        var lastBorder = new Border(-1, -1, -1);
        while (iterator.hasNext()) {
            var current = iterator.next();
            if (current.d1 != lastBorder.d1 || current.type != lastBorder.type || current.d2 != lastBorder.d2 + 1) {
                sides += 1;
            }
            lastBorder = current;
        }

        return sides;
    }

    private int findEmptyPos(int startPos) {
        int i;
        for (i = startPos; i < lc.state.length; i++) {
            if (lc.state[i] == 0) {
                return i;
            }
        }

        return i;
    }

    private Cost floodfill(int y, int x, char plant, TreeSet<Border> borders, int areaId) {

        if (y < 0 || y >= lc.h || x < 0 || x >= lc.w) {
            return new Cost(0, 0);
        }
        if (lc.map[y][x] != plant || lc.state[y * lc.w + x] != 0) {
            return new Cost(0, 0);
        }

        // neues Element besetzen
        lc.state[y * lc.w + x] = areaId;

        var perimeter = 0;
        if (y - 1 < 0 || lc.map[y - 1][x] != plant) {
            perimeter++;
            borders.add(new Border(y, x, BT_TOP));
        }
        if (y + 1 >= lc.h || lc.map[y + 1][x] != plant) {
            perimeter++;
            borders.add(new Border(y + 1, x, BT_BOTTOM));
        }
        if (x - 1 < 0 || lc.map[y][x - 1] != plant) {
            perimeter++;
            borders.add(new Border(x - 1, y, BT_LEFT));
        }
        if (x + 1 >= lc.w || lc.map[y][x + 1] != plant) {
            perimeter++;
            borders.add(new Border(x + 1, y, BT_RIGHT));
        }

        var ret = new Cost(1, perimeter);

        ret.add(floodfill(y - 1, x, plant, borders, areaId));
        ret.add(floodfill(y, x + 1, plant, borders, areaId));
        ret.add(floodfill(y + 1, x, plant, borders, areaId));
        ret.add(floodfill(y, x - 1, plant, borders, areaId));

        return ret;
    }

    private void dumpMap(Pdata lc) {

        String[] bglist = new String[]{
                BG_RED,
                BG_GREEN,
                BG_YELLOW,
                BG_BLUE,
                BG_PURPLE,
                BG_CYAN,
                BG_WHITE
        };

        for (var y = 0; y < lc.h; y++) {
            for (var x = 0; x < lc.w; x++) {
                System.out.print(BLACK);
                System.out.print(bglist[lc.state[y * lc.w + x] % bglist.length]);
                System.out.print(lc.map[y][x]);
                System.out.print(" ");
                System.out.print(RESET);
            }
            System.out.println();
        }
        System.out.print(RESET);
    }
}
