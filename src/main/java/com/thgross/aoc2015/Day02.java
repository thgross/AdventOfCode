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

        int paperSize = 0;
        int ribbonLength = 0;

        var areas = new int[3];
        var circumfences = new int[3];
        var box = new int[3];

        for (String line : lines) {
            var parts = line.split("x");
            for (int i = 0; i < parts.length; i++) {
                box[i] = Integer.parseInt(parts[i]);
            }

            // Part 1
            areas[0] = box[0] * box[1];
            areas[1] = box[1] * box[2];
            areas[2] = box[2] * box[0];
            Arrays.sort(areas);

            paperSize += areas[0] * 3 + areas[1] * 2 + areas[2] * 2;

            // Part 2
            circumfences[0] = 2 * box[0] + 2 * box[1];
            circumfences[1] = 2 * box[1] + 2 * box[2];
            circumfences[2] = 2 * box[2] + 2 * box[0];
            Arrays.sort(circumfences);

            ribbonLength += circumfences[0] + box[0] * box[1] * box[2];
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 square feet: %d\n", paperSize);
        System.out.printf("Part 2 ribbon length: %d\n", ribbonLength);
    }
}
