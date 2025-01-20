package com.thgross.aoc2015;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day05 extends Application {

    String inputFilename = "aoc2015/input05.txt";

    public static void main(String[] args) {
        var app = (new Day05());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int nice = 0;

        var p1 = Pattern.compile("[aeiou].*[aeiou].*[aeiou]");
        var p2 = Pattern.compile("(.)\\1");
        var p3 = Pattern.compile("(ab|cd|pq|xy)");

        for (String line : lines) {
            if (!p1.matcher(line).find()) {
                continue;
            }
            if (!p2.matcher(line).find()) {
                continue;
            }
            if (p3.matcher(line).find()) {
                continue;
            }

            nice++;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: nice strings: %d\n", nice);
    }
}
