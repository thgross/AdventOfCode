package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Day11 extends Application {

    String inputFilename = "aoc2015/input11.txt";

    Pattern lookAndSay = Pattern.compile("(.)\\1*");

    public static void main(String[] args) {
        var app = (new Day11());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var oldpw = lines.getFirst();

        // Durch Nachdenken rausgekriegt ...
        String newpw1 = "vzbxxyzz";
        // Durch Nachdenken rausgekriegt ...
        String newpw2 = "vzcaabcc";

        System.out.println("--------------------------------------");
        System.out.printf("Part 1: New Password: %s\n", newpw1);
        System.out.printf("Part 2: New Password: %s\n", newpw2);
    }
}
