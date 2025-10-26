package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 extends Application {

    String inputFilename = "aoc2016/input08.txt";

    public static void main(String[] args) {
        var app = (new Day08());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        int part1On;

        var sc = new Screen();
        sc.init(6, 50);

        var patternRect = Pattern.compile("rect (\\d+)x(\\d+)");
        var patternRotateRow = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
        var patternRotateCol = Pattern.compile("rotate column x=(\\d+) by (\\d+)");

        Matcher matcher;

        for (String line : lines) {
            matcher = patternRect.matcher(line);
            if (matcher.find()) {
                sc.rect(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            matcher = patternRotateRow.matcher(line);
            if (matcher.find()) {
                sc.rotateRow(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            matcher = patternRotateCol.matcher(line);
            if (matcher.find()) {
                sc.rotateCol(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
        }

        part1On = sc.printScreen(false);


        System.out.printf("Part 1 Pixels on: %d\n", part1On);
//        System.out.printf("Part 2 SSL support: %s\n", sslSupport);
    }

    static class Screen {
        int sh, sw;
        boolean[][] screen;
        boolean[] tmprow;
        boolean[] tmpcol;

        @SuppressWarnings("SameParameterValue")
        void init(int h, int w) {
            screen = new boolean[h][w];
            tmprow = new boolean[w];
            tmpcol = new boolean[h];
            sh = h;
            sw = w;
        }

        void rect(int w, int h) {
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    screen[row][col] = true;
                }
            }
        }

        void rotateRow(int row, int length) {
            for (int i = 0; i < sw; i++) {
                tmprow[(i + length) % sw] = screen[row][i];
            }
            System.arraycopy(tmprow, 0, screen[row], 0, sw);
        }

        void rotateCol(int col, int length) {
            for (int i = 0; i < sh; i++) {
                tmpcol[(i + length) % sh] = screen[i][col];
            }
            for (int i = 0; i < sh; i++) {
                screen[i][col] = tmpcol[i];
            }
        }

        public int printScreen(boolean countPixelsOnly) {
            int countPixelsOn = 0;
            for (int row = 0; row < sh; row++) {
                for (int col = 0; col < sw; col++) {

                    if (screen[row][col]) {
                        countPixelsOn++;
                        if (!countPixelsOnly) {
                            System.out.print("#"); // Für true
                        }
                    } else {
                        if (!countPixelsOnly) {
                            System.out.print("."); // Für false
                        }
                    }
                }
                if (!countPixelsOnly) {
                    System.out.println();
                }
            }
            return countPixelsOn;
        }
    }
}