package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Day10 extends Application {

    String inputFilename = "aoc2015/input10.txt";

    Pattern lookAndSay = Pattern.compile("(.)\\1*");

    public static void main(String[] args) {
        var app = (new Day10());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int length = 0;
        int length2 = 0;

        var text = lines.getFirst();
        for (int i = 0; i < 40; i++) {
            text = lookAndSay(text);
        }
        length = text.length();

        text = lines.getFirst();
        for (int i = 0; i < 50; i++) {
            text = lookAndSay(text);
        }
        length2 = text.length();

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Result Length: %d\n", length);
        System.out.printf("Part 2: Result Length: %d\n", length2);
    }

    protected String lookAndSay(String text) {

        var newtext = new StringBuilder();

        var matcher = lookAndSay.matcher(text);
        while (matcher.find()) {
            for (int gr = 0; gr < matcher.groupCount(); gr++) {
                var mg = matcher.group(gr);
                newtext.append(mg.length());
                newtext.append(mg.charAt(0));
            }
        }

        return newtext.toString();
    }
}
