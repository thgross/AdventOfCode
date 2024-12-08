package com.thgross;

import com.thgross.aoc.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AOC2024_08 extends Application {
    public static void main(String[] args) {
        (new AOC2024_08()).run();
    }

    public final static int LVLMAP = 0;
    public final static int LVLANTINODE = 1;
    public final static int LVLALLANTINODES = 2;

    public static class Pos {
        int x, y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Pdata {
        char[][][] map;
        Map<Character, List<Pos>> tgroups = new HashMap<>();
        int w, h;
        int antinodes = 0;
        int allantinodes = 0;
    }

    private final Pdata lc = new Pdata();

    @Override
    public void run() {
        try {
            Instant start = Instant.now();
            calcAll("input08.txt");
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
            lc.map = new char[3][lc.h][lc.w];

            // Map einlesen
            for (int y = 0; y < slines.size(); y++) {
                var line = slines.get(y);
                line.getChars(0, line.length(), lc.map[LVLMAP][y], 0);
            }

            // Tower-Groups erzeugen
            for (int y = 0; y < lc.map[LVLMAP].length; y++) {
                for (int x = 0; x < lc.map[LVLMAP][y].length; x++) {
                    lc.map[LVLANTINODE][y][x] = ' ';
                    lc.map[LVLALLANTINODES][y][x] = ' ';
                    var mapchar = lc.map[LVLMAP][y][x];
                    if (mapchar == '.') {
                        continue;
                    }
                    List<Pos> tpos;
                    if (lc.tgroups.containsKey(mapchar)) {
                        tpos = lc.tgroups.get(mapchar);
                    } else {
                        tpos = new ArrayList<>();
                        lc.tgroups.put(mapchar, tpos);
                    }
                    tpos.add(new Pos(x, y));
                }
            }

            // Teil 1. Alle Tower-Groups iterieren und Antinodes erzeugen
            lc.tgroups.forEach((chr, posList) -> {
                if (posList.size() > 1) {
                    // Paare ermitteln
                    for (var t1 = 0; t1 < posList.size() - 1; t1++) {
                        for (var t2 = t1 + 1; t2 < posList.size(); t2++) {
                            buildAntinodes(posList.get(t1), posList.get(t2));
                            buildAllAntinodes(posList.get(t1), posList.get(t2));
                        }
                    }
                }
            });

            lc.antinodes = countAntinodes(lc.map[LVLANTINODE]);
            lc.allantinodes = countAntinodes(lc.map[LVLALLANTINODES]);

            System.out.printf("Antinodes: %d\n", lc.antinodes);
            System.out.printf("All Antinodes: %d\n", lc.allantinodes);
        }
    }

    public int countAntinodes(char[][] map) {
        var count = 0;

        for (char[] chars : map) {
            for (char aChar : chars) {
                if (aChar == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    public void buildAntinodes(Pos t1, Pos t2) {

        double midx = (float) (t1.x + t2.x) / 2.0;
        double midy = (float) (t1.y + t2.y) / 2.0;
        double offx = (float) (t1.x - t2.x) * 1.5;
        double offy = (float) (t1.y - t2.y) * 1.5;

        int p1x = (int) (midx - offx);
        int p1y = (int) (midy - offy);
        int p2x = (int) (midx + offx);
        int p2y = (int) (midy + offy);

        if (p1x >= 0 && p1x < lc.w && p1y >= 0 && p1y < lc.h) {
            lc.map[LVLANTINODE][p1y][p1x] = '#';
        }
        if (p2x >= 0 && p2x < lc.w && p2y >= 0 && p2y < lc.h) {
            lc.map[LVLANTINODE][p2y][p2x] = '#';
        }
    }

    public void buildAllAntinodes(Pos t1, Pos t2) {

        int offx = t1.x - t2.x;
        int offy = t1.y - t2.y;

        int px = t1.x;
        int py = t1.y;

        while (px >= 0 && px < lc.w && py >= 0 && py < lc.h) {
            lc.map[LVLALLANTINODES][py][px] = '#';
            px -= offx;
            py -= offy;
        }

        px = t1.x;
        py = t1.y;
        while (px >= 0 && px < lc.w && py >= 0 && py < lc.h) {
            lc.map[LVLALLANTINODES][py][px] = '#';
            px += offx;
            py += offy;
        }
    }
}
