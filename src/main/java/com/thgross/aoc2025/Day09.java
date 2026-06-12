package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import com.thgross.aoc.Point3D;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 extends Application {

    String inputFilename = "aoc2025/input09.txt";

    static void main() {
        var app = (new Day09());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1LargestRectSize = 0;
        long tmpRectSize = 0;

        List<Point> points = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }

        for (int i1 = 0; i1 < points.size(); i1++) {
            for (int i2 = i1 + 1; i2 < points.size(); i2++) {
                Point p1 = points.get(i1);
                Point p2 = points.get(i2);
                tmpRectSize = Math.abs((1L + p1.x - p2.x) * (1L + p1.y - p2.y));
                if (tmpRectSize > part1LargestRectSize) {
                    part1LargestRectSize = tmpRectSize;
                }
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Largest Rectangle: %d\n", part1LargestRectSize);
    }
}
