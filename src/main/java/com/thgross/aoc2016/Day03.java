package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day03 extends Application {

    String inputFilename = "aoc2016/input03.txt";

    public static void main(String[] args) {
        var app = (new Day03());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        var triangles = new ArrayList<Integer[]>();
        var numTriangles = 0;
        var numRealTriangles = 0;
        var numRealTrianglesPart2 = 0;

        for (String line : lines) {
            triangles.add(new Integer[]{
                    Integer.parseInt(line.substring(0, 5).trim()),
                    Integer.parseInt(line.substring(5, 10).trim()),
                    Integer.parseInt(line.substring(10, 15).trim())
            });
            numTriangles++;
            var tmpTriangle = triangles.getLast().clone();
            Arrays.sort(tmpTriangle);
            if (tmpTriangle[2] < tmpTriangle[0] + tmpTriangle[1]) {
                numRealTriangles++;
            }
        }

        for (int tId = 0; tId < triangles.size(); tId += 3) {
            for (int col = 0; col < 3; col++) {
                var tmpTriangle = new Integer[]{
                        triangles.get(tId)[col],
                        triangles.get(tId + 1)[col],
                        triangles.get(tId + 2)[col]
                };
                Arrays.sort(tmpTriangle);
                if (tmpTriangle[2] < tmpTriangle[0] + tmpTriangle[1]) {
                    numRealTrianglesPart2++;
                }
            }
        }

        System.out.printf("Part 1 Triangles: %d\n", numTriangles);
        System.out.printf("Part 1 real Triangles: %d\n", numRealTriangles);
        System.out.printf("Part 2 real Triangles: %d\n", numRealTrianglesPart2);
    }
}