package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Stream;

public class AOC2024_10_Win extends Application {

    Testframe frame;
    Image imgMain;

    public static void main(String[] args) {
        var app = (new AOC2024_10_Win());
        app.frame = new Testframe();
        app.run();
    }

    static class Pdata {
        int[][] map;
        int w, h;
        int trailheads, trailheadsScore, trailheadsRatings;
    }

    static class Testframe extends Frame {
        Graphics2D g;
        int gw = 20;
        int gh = 20;
        int lastDrawX = -1;
        int lastDrawY = -1;

        public Testframe() {
            setTitle("AOC2024_10");
            setSize(1040, 1040);
            addWindowListener(new TestWindowListener());

            setVisible(true);

            g = (Graphics2D) getGraphics();

//            redraw();
        }

        public void redraw() {
            g.drawLine(100, 100, 300, 300);
        }
    }

    static class TestWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            event.getWindow().dispose();
            System.exit(0);
        }
    }

    // oben, rechts, unten, links
    private final int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private final AOC2024_10_Win.Pdata lc = new AOC2024_10_Win.Pdata();

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
                    if (!rating) {
                        drawBox(map, nx, ny);
                        map[ny][nx] = 19;    // merken, dass wir hier schon waren
                    }
                    score += 1;
                } else {
                    score += calcScore(map, ny, nx, rating);
                }
            }
        }

        if (!rating && score > 0) {
            drawBox(map, x, y);
        }


        return score;
    }

    private void drawBox(int[][] map, int x, int y) {
        frame.g.setColor(new Color(200 - (map[y][x] % 10) * 20, 0, 0));
        frame.g.fillRect(50 + x * frame.gw, 80 + y * frame.gh, frame.gw, frame.gh);

        if (frame.lastDrawX != -1 && frame.lastDrawY != -1) {
            frame.g.setColor(Color.MAGENTA);
            frame.g.setStroke(new BasicStroke(4));
            frame.g.drawLine(
                    50 + x * frame.gw + frame.gw / 2, 80 + y * frame.gh + frame.gh / 2,
                    50 + frame.lastDrawX * frame.gw + frame.gw / 2, 80 + frame.lastDrawY * frame.gh + frame.gh / 2
            );
        }

        frame.g.setColor(Color.yellow);
        frame.g.drawString(String.valueOf(map[y][x]), 50 + x * frame.gw + (int) (frame.gw * 0.3), 80 + y * frame.gh + 10 + (int) (frame.gh * 0.2));

        if (map[y][x] == 0) {
            frame.lastDrawX = -1;
            frame.lastDrawY = -1;
        } else {
            frame.lastDrawX = x;
            frame.lastDrawY = y;
        }
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
