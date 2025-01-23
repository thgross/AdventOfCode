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

        int nice1 = 0;
        int nice2 = 0;

        var p1a = Pattern.compile("[aeiou].*[aeiou].*[aeiou]");
        var p1b = Pattern.compile("(.)\\1");
        var p1c = Pattern.compile("(ab|cd|pq|xy)");

        var p2a = Pattern.compile("(..).*\\1");
        var p2b = Pattern.compile("(.).\\1");

        for (String line : lines) {
            if ((p1a.matcher(line).find()) && (p1b.matcher(line).find()) && (!p1c.matcher(line).find())) {
                nice1++;
            }

            if ((p2a.matcher(line).find()) && (p2b.matcher(line).find())) {
                nice2++;
            }

        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: nice strings: %d\n", nice1);
        System.out.printf("Part 2: nice strings: %d\n", nice2);
    }
}
