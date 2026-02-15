package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day06 extends Application {

    String inputFilename = "aoc2025/input06-t1.txt";

    public static void main(String[] args) {
        var app = (new Day06());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1Total = 0;

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Total: %d\n", part1Total);
    }
}
