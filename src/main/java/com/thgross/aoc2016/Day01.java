package com.thgross.aoc2016;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Day01 extends Application {

    String inputFilename = "aoc2016/input01.txt";

    public static void main(String[] args) {
        var app = (new Day01());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        Pos pos = new Pos(0, 0);
        Pos posFirstDoubleVisit = null;
        int dir = 0;
        List<String> positionHashes = new ArrayList<>();

        var matcher = (Pattern.compile("([RL])(\\d+)")).matcher(lines.getFirst());
        while (matcher.find()) {
            if (matcher.groupCount() != 2) {
                throw new RuntimeException("mehr oder weniger als 2 Elemente.");
            }
            if (Objects.equals(matcher.group(1), "R")) {
                dir = Math.floorMod(dir + 1, 4);
            } else {
                dir = Math.floorMod(dir - 1, 4);
            }

//            System.out.printf("Dir: %d \n", dir);

            var shortDistance = Integer.parseInt(matcher.group(2));

            // Neue Position berechnen
            var posDest = new Pos(pos.y + dirs[dir].y * shortDistance, pos.x + dirs[dir].x * shortDistance);

            while (pos.x != posDest.x || pos.y != posDest.y) {
                pos.x += dirs[dir].x;
                pos.y += dirs[dir].y;
                var poshash = "x:" + pos.x + " y:" + pos.y;
                if (positionHashes.contains(poshash) && posFirstDoubleVisit == null) {
                    posFirstDoubleVisit = pos.copy();
                } else {
                    positionHashes.add(poshash);
                }
            }
        }

        var distance = Math.abs(pos.x) + Math.abs(pos.y);

        if (posFirstDoubleVisit == null) {
            throw new RuntimeException("no location found that is visited twice!");
        }
        var distancePart2 = Math.abs(posFirstDoubleVisit.x) + Math.abs(posFirstDoubleVisit.y);

        System.out.printf("Part 1 Pos: %d,%d\n", pos.x, pos.y);
        System.out.printf("Part 1 Distance: %d\n", distance);
        System.out.printf("Part 2 Pos: %d,%d\n", posFirstDoubleVisit.x, posFirstDoubleVisit.y);
        System.out.printf("Part 2 Distance: %d\n", distancePart2);
//        System.out.printf("Similarity Score: %d\n", lambdaContext.totalSimilarityScore);
    }
}