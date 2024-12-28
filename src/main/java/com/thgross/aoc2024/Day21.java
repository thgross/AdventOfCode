package com.thgross.aoc2024;

import com.thgross.aoc.Application;
import com.thgross.aoc.StringMap;
import com.thgross.aoc2024.day21.Dirpad;
import com.thgross.aoc2024.day21.Numpad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day21 extends Application {

    String inputFilename = "aoc2024/input21-t1b.txt";

    public static void main(String[] args) {
        var app = (new Day21());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<List<Character>> codes = new ArrayList<>();
        for (String line : lines) {
            List<Character> code = new ArrayList<>();
            for (char c : line.toCharArray()) {
                code.add(c);

            }
            codes.add(code);
        }

        var smap = new StringMap(6, 40, BLACK_BRIGHT + "·" + RESET);

        var mypad = new Dirpad();
        var dpad2 = new Dirpad();
        var dpad1 = new Dirpad();
        var nppad = new Numpad();

        System.out.println("----------------------------------");
        // Part 1
        Long part1Complexity = 0L;
        List<Character> dpPaths1;
        List<Character> dpPaths2;
        List<Character> myPath;
        mypad.reset();
        mypad.connect(dpad2);
        dpad2.reset();
        dpad2.connect(dpad1);
        dpad1.reset();
        dpad1.connect(nppad);
        nppad.reset();

        nppad.dump();
        dpad1.dump();

        for (List<Character> code : codes) {
            dpPaths1 = nppad.getPaths(code);
            dpPaths2 = dpad1.getPaths(dpPaths1);
            myPath = dpad2.getPaths(dpPaths2);

            var pathString = code.stream()
                    .map(e -> e.toString())
                    .collect(Collectors.joining());
            var codeVal = (long) Integer.parseInt(pathString.substring(0, 3));
            part1Complexity += codeVal * myPath.size();

            System.out.printf("%s: %s\n", pathToString(code), pathToString(myPath));

            for (Character c : myPath) {
                mypad.press(c);
                mypad.dumpToStringMap(smap.smap, 0, 0);
                dpad2.dumpToStringMap(smap.smap, 0, 10);
                dpad1.dumpToStringMap(smap.smap, 0, 20);
                nppad.dumpToStringMap(smap.smap, 0, 30);
                smap.dump();
            }
        }
        System.out.println("------------------------------------");
        System.out.printf("Part 1 Complexity: %d\n", part1Complexity);
    }

    private String pathToString(List<Character> path) {
        StringBuilder sb = new StringBuilder();
        for (Character c : path) {
            sb.append(c);
        }

        return sb.toString();
    }
}
