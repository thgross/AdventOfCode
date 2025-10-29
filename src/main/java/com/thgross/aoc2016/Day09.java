package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Application {

    String inputFilename = "aoc2016/input09.txt";

    Pattern patternMarker = Pattern.compile("\\((\\d+)x(\\d+)\\)");
    Matcher matcher;

    public static void main(String[] args) {
        var app = (new Day09());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        StringBuilder sbPart1 = new StringBuilder();
        int part1Length;
        long part2Length;

        var line = lines.getFirst();
        int lpos = 0;

        int markerLength, markerPosition, markerCharcount, markerRepetitions;
        String part;

        do {
            matcher = patternMarker.matcher(line);
            if (matcher.find(lpos)) {
                markerLength = matcher.group().length();
                markerPosition = matcher.start();
                markerCharcount = Integer.parseInt(matcher.group(1));
                markerRepetitions = Integer.parseInt(matcher.group(2));
                if (markerPosition > lpos) {
                    // Vor dem Marker sind noch Zeichen
                    sbPart1.append(line, lpos, markerPosition);
                }
                lpos = markerPosition + markerLength;
                part = line.substring(lpos, lpos + markerCharcount);
                // Subpart wiederholen
                sbPart1.append(part.repeat(markerRepetitions));
                lpos += markerCharcount;
//                System.out.printf("%s [%d]: '%s'\n", matcher.group(0), markerLength, part);
            } else {
                if (lpos < line.length()) {
                    // Nach dem letzten Marker sind noch Zeichen übrig
                    sbPart1.append(line, lpos, line.length());
                }
                break;
            }
        } while (true);

        part1Length = sbPart1.length();

        // Part 2
        part2Length = calcSequenceSize(line, 0, line.length());

        System.out.printf("Part 1 Length: %d\n", part1Length);
        System.out.printf("Part 2 Length: %d\n", part2Length);
    }

    long calcSequenceSize(String line, int start, int end) {
        long count = 0;
        int markerLength, markerPosition, markerCharcount, markerRepetitions;
        do {
            matcher = patternMarker.matcher(line);
            if (matcher.find(start) && matcher.start() < end) {
                markerLength = matcher.group().length();
                markerPosition = matcher.start();
                markerCharcount = Integer.parseInt(matcher.group(1));
                markerRepetitions = Integer.parseInt(matcher.group(2));
                if (markerPosition > start) {
                    // Vor dem Marker sind noch Zeichen
                    count += markerPosition - start;
                }
                start = markerPosition + markerLength;
                count += markerRepetitions * calcSequenceSize(line, start, start + markerCharcount);
                start += markerCharcount;
            } else {
                if (start < end) {
                    // Nach dem letzten Marker sind noch Zeichen übrig
                    count += end - start;
                }
                break;
            }
        } while (true);

        return count;
    }
}