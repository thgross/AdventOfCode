package com.thgross;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class AOC2024_12 extends Application {
    public static void main(String[] args) {
        (new AOC2024_12()).run();
    }

    private static class VBorder implements Comparable<VBorder> {
        int y, x;
        int side;

        public VBorder(int y, int x, int side) {
            this.y = y;
            this.x = x;
            this.side = side;
        }

        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            VBorder vBorder = (VBorder) o;
            return y == vBorder.y && x == vBorder.x && side == vBorder.side;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x, side);
        }

        @Override
        public int compareTo(VBorder b2) {
            if (x < b2.x) {
                return -1;
            }
            if (x > b2.x) {
                return 1;
            }
            if (side < b2.side) {
                return -1;
            }
            if (side > b2.side) {
                return 1;
            }
            // ab hier ist x und die Seite gleich!
            return Integer.compare(y, b2.y);
        }
    }

    private static class HBorder implements Comparable<HBorder> {
        int y, x;
        int side;

        public HBorder(int y, int x, int side) {
            this.y = y;
            this.x = x;
            this.side = side;
        }

        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            HBorder hBorder = (HBorder) o;
            return y == hBorder.y && x == hBorder.x && side == hBorder.side;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x, side);
        }

        @Override
        public int compareTo(HBorder b2) {
            if (y < b2.y) {
                return -1;
            }
            if (y > b2.y) {
                return 1;
            }
            if (side < b2.side) {
                return -1;
            }
            if (side > b2.side) {
                return 1;
            }
            // ab hier ist y gleich!
            return Integer.compare(x, b2.x);
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
        byte[] state;
        int emptyPos;
        int w, h;
        Instant start;
        Instant stop;
    }

    private final AOC2024_12.Pdata lc = new AOC2024_12.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("input12.txt");
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
        lc.state = new byte[lc.h * lc.w];
        Arrays.fill(lc.state, (byte) 0);

        lc.emptyPos = 0;

        // Map einlesen
        for (int y = 0; y < slines.size(); y++) {
            var line = slines.get(y);
            line.getChars(0, line.length(), lc.map[y], 0);
        }

        var price1 = 0;
        var price2 = 0;
        while (lc.emptyPos < lc.state.length) {
            int y = lc.emptyPos / lc.w;
            int x = lc.emptyPos % lc.w;
            var bListV = new TreeSet<VBorder>();
            var bListH = new TreeSet<HBorder>();
            var cost = floodfill(y, x, lc.map[y][x], bListH, bListV);
            var sides = calcSides(bListH, bListV);
            price1 += cost.area * cost.perimeter;
            price2 += cost.area * sides;
            System.out.printf("%s: %3d t / %3d b / %4d s | P1: %5d | P2: %5d\n", lc.map[y][x], cost.area, cost.perimeter, sides, cost.area * cost.perimeter, cost.area * sides);
            lc.emptyPos = findEmptyPos(lc.emptyPos);
        }
        System.out.println();

        System.out.printf("Part 1 Price: %d\n", price1);
        System.out.printf("Part 2 Price: %d\n", price2);
    }

    private int calcSides(TreeSet<HBorder> hBorders, TreeSet<VBorder> vBorders) {

        int sides = 0;
        // horizontale Ränder
        var sortedHBorders = hBorders.stream().sorted();
        Iterator<HBorder> itrH = sortedHBorders.iterator();
        var lastHBorder = new HBorder(-1, -1, -1);
        while (itrH.hasNext()) {
            var current = itrH.next();
            if (current.y != lastHBorder.y || current.side != lastHBorder.side || current.x != lastHBorder.x + 1) {
                sides += 1;
            }
            lastHBorder = current;
        }

        // vertikale Ränder
        var sortedVBorders = vBorders.stream().sorted();
        Iterator<VBorder> itrV = sortedVBorders.iterator();
        var lastVBorder = new VBorder(-1, -1, -1);
        while (itrV.hasNext()) {
            var current = itrV.next();
            if (current.x != lastVBorder.x || current.side != lastVBorder.side || current.y != lastVBorder.y + 1) {
                sides += 1;
            }
            lastVBorder = current;
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

    private Cost floodfill(int y, int x, char plant, TreeSet<HBorder> hBorders, TreeSet<VBorder> vBorders) {

        if (y < 0 || y >= lc.h || x < 0 || x >= lc.w) {
            return new Cost(0, 0);
        }
        if (lc.map[y][x] != plant || lc.state[y * lc.w + x] != 0) {
            return new Cost(0, 0);
        }

        // neues Element besetzen
        lc.state[y * lc.w + x] = 1;

        var perimeter = 0;
        if (y - 1 < 0 || lc.map[y - 1][x] != plant) {
            perimeter++;
            hBorders.add(new HBorder(y, x, 0));
        }
        if (y + 1 >= lc.h || lc.map[y + 1][x] != plant) {
            perimeter++;
            hBorders.add(new HBorder(y + 1, x, 1));
        }
        if (x - 1 < 0 || lc.map[y][x - 1] != plant) {
            perimeter++;
            vBorders.add(new VBorder(y, x, 0));
        }
        if (x + 1 >= lc.w || lc.map[y][x + 1] != plant) {
            perimeter++;
            vBorders.add(new VBorder(y, x + 1, 1));
        }

        var ret = new Cost(1, perimeter);

        ret.add(floodfill(y - 1, x, plant, hBorders, vBorders));
        ret.add(floodfill(y, x + 1, plant, hBorders, vBorders));
        ret.add(floodfill(y + 1, x, plant, hBorders, vBorders));
        ret.add(floodfill(y, x - 1, plant, hBorders, vBorders));

        return ret;
    }
}
