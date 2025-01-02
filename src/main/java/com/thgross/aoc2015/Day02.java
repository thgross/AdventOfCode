package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day02 extends Application {

    String inputFilename = "aoc2015/input02.txt";

    public static void main(String[] args) {
        var app = (new Day02());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long paperSize = 0;
        long ribbonLength = 0;

        int[][] boxes = new int[lines.size()][3];
        var areas = new int[3];
        var circumfences = new int[3];
//        int dmin, d1, d2;
        for (int i = 0; i < lines.size(); i++) {
//            dmin = -1;
            var parts = lines.get(i).split("x");
            for (int i1 = 0; i1 < parts.length; i1++) {
                boxes[i][i1] = Integer.parseInt(parts[i1]);
            }

            // Part 1
            areas[0] = boxes[i][0] * boxes[i][1];
            areas[1] = boxes[i][1] * boxes[i][2];
            areas[2] = boxes[i][2] * boxes[i][0];
            Arrays.sort(areas);

            paperSize += areas[0] * 3L + areas[1] * 2L + areas[2] * 2L;

            // Part 2
            circumfences[0] = 2 * boxes[i][0] + 2 * boxes[i][1];
            circumfences[1] = 2 * boxes[i][1] + 2 * boxes[i][2];
            circumfences[2] = 2 * boxes[i][2] + 2 * boxes[i][0];
            Arrays.sort(circumfences);

            ribbonLength += circumfences[0] + (long) boxes[i][0] * boxes[i][1] * boxes[i][2];
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 square feet: %d\n", paperSize);
        System.out.printf("Part 2 ribbon length: %d\n", ribbonLength);
    }
}
