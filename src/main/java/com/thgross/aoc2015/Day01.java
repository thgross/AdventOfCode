package com.thgross.aoc2015;

import com.thgross.aoc.Application;
import com.thgross.aoc2024.day25.Heightmap;
import com.thgross.aoc2024.day25.Key;
import com.thgross.aoc2024.day25.Lock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day01 extends Application {

    String inputFilename = "aoc2015/input01.txt";

    public static void main(String[] args) {
        var app = (new Day01());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<Character> chars = new ArrayList<>();

        int basementSteps = 0;
        int currentFloor = 0;

        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                var chr = line.charAt(i);
                chars.add(chr);
                if (chr == '(') {
                    currentFloor++;
                } else {
                    currentFloor--;
                }
                if (currentFloor == -1 && basementSteps == 0) {
                    basementSteps = i + 1;
                }
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Final floor: %d\n", currentFloor);
        System.out.printf("Basement reached at position: %d\n", basementSteps);
    }
}
