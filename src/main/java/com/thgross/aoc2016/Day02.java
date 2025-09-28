package com.thgross.aoc2016;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.List;

public class Day02 extends Application {

    String inputFilename = "aoc2016/input02.txt";

    public static void main(String[] args) {
        var app = (new Day02());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        var buttonsPart2 = new String[][]{
                {null, null, null, null, null, null, null},
                {null, null, null, "1", null, null, null},
                {null, null, "2", "3", "4", null, null},
                {null, "5", "6", "7", "8", "9", null},
                {null, null, "A", "B", "C", null, null},
                {null, null, null, "D", null, null, null},
                {null, null, null, null, null, null, null},
        };

        var padpos = new Pos(1, 1);
        var padpos2 = new Pos(3, 1);
        StringBuilder bathroomcode = new StringBuilder();
        StringBuilder bathroomcode2 = new StringBuilder();

        for (String line : lines) {
            for (char c : line.toCharArray()) {
                var dir = switch (c) {
                    case 'U' -> 0;
                    case 'R' -> 1;
                    case 'D' -> 2;
                    case 'L' -> 3;
                    default -> throw new IllegalStateException("Unexpected value: " + c);
                };

                padpos.add(dir);
                if (padpos.x < 0) padpos.x = 0;
                if (padpos.x > 2) padpos.x = 2;
                if (padpos.y < 0) padpos.y = 0;
                if (padpos.y > 2) padpos.y = 2;

                padpos2.add(dir);
                if (buttonsPart2[padpos2.y][padpos2.x] == null) {
                    // step back
                    padpos2.sub(dir % 4);
                }
            }

            // Press Button
            bathroomcode.append(padpos.y * 3 + padpos.x + 1);
            bathroomcode2.append(buttonsPart2[padpos2.y][padpos2.x]);
        }

        System.out.printf("Part 1 Bathroomcode: %s\n", bathroomcode);
        System.out.printf("Part 2 Bathroomcode: %s\n", bathroomcode2);
//        System.out.printf("Part 1 Distance: %d\n", distance);
//        System.out.printf("Part 2 Pos: %d,%d\n", posFirstDoubleVisit.x, posFirstDoubleVisit.y);
//        System.out.printf("Part 2 Distance: %d\n", distancePart2);
    }
}