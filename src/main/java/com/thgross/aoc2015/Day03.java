package com.thgross.aoc2015;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day03 extends Application {

    String inputFilename = "aoc2015/input03.txt";

    public static void main(String[] args) {
        var app = (new Day03());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        // Part 1
        int houses1;
        Pos pos1 = new Pos(0, 0);
        Map<String, Integer> map1 = new HashMap<>();

        map1.put(String.format("%s_%s", pos1.y, pos1.x), 1);
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                pos1.addDir(c);
                map1.put(String.format("%s_%s", pos1.y, pos1.x), 1);
            }
        }

        houses1 = map1.size();

        // Part 2
        int houses2;
        int currentSanta = 0;
        var pos = new ArrayList<Pos>() {{
            add(new Pos(0, 0));
            add(new Pos(0, 0));
        }};
        var map2 = new HashMap<>();

        map2.put(String.format("%s_%s", 0, 0), 1);
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                pos.get(currentSanta).addDir(c);
                map2.put(String.format("%s_%s", pos.get(currentSanta).y, pos.get(currentSanta).x), 1);

                currentSanta = 1 - currentSanta;
            }
        }

        houses2 = map2.size();

        System.out.println("------------------------------------");
        System.out.printf("Part 1 houses: %d\n", houses1);
        System.out.printf("Part 2 houses: %d\n", houses2);
    }
}
