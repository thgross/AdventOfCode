package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day05 extends Application {

    String inputFilename = "aoc2025/input05-t1.txt";

    public static void main(String[] args) {
        var app = (new Day05());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int part1Fresh = 0;
        record Range(long from, long to) {
        }

        var ranges = new ArrayList<Range>();
        var ingreds = new ArrayList<Long>();

        var patternRange = Pattern.compile("^(\\d+)-(\\d+)$");
        var patternIngredient = Pattern.compile("^(\\d+)$");


        for (String line : lines) {
            var matchRange = patternRange.matcher(line);
            var matchIngred = patternIngredient.matcher(line);
            if (matchRange.matches()) {
                ranges.add(new Range(Long.parseLong(matchRange.group(1)), Long.parseLong(matchRange.group(2))));
            } else if (matchIngred.matches()) {
                ingreds.add(Long.parseLong(matchIngred.group(1)));
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 rolls: %d\n", part1Fresh);
//        System.out.printf("Part 2 rolls: %d\n", part2Rolls);
    }
}
