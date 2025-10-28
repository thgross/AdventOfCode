package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Application {

    String inputFilename = "aoc2016/input09.txt";

    public static void main(String[] args) {
        var app = (new Day09());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        StringBuilder sbPart1 = new StringBuilder();
        int part1Length;

        var line = lines.getFirst();
        int lpos = 0;

        var patternMarker = Pattern.compile("\\((\\d+)x(\\d+)\\)");
        Matcher matcher;

        int markerLength, markerPosition, charCount, repetitions;
        String part;

        do {
            matcher = patternMarker.matcher(line);
            if (matcher.find(lpos)) {
                markerLength = matcher.group().length();
                markerPosition = matcher.start();
                charCount = Integer.parseInt(matcher.group(1));
                repetitions = Integer.parseInt(matcher.group(2));
                if(markerPosition > lpos) {
                    sbPart1.append(line, lpos, markerPosition);
                }
                lpos = markerPosition + markerLength;
                part = line.substring(lpos, lpos + charCount);
                sbPart1.append(part.repeat(repetitions));
                lpos += charCount;
//                System.out.printf("%s [%d]: '%s'\n", matcher.group(0), markerLength, part);
            } else {
                if(lpos < line.length()) {
                    sbPart1.append(line, lpos, line.length());
                }
                break;
            }
        } while (true);

//        System.out.println(sbPart1);
        part1Length = sbPart1.length();

        System.out.printf("Part 1 Length: %d\n", part1Length);
//        System.out.printf("Part 2 SSL support: %s\n", sslSupport);
    }
}