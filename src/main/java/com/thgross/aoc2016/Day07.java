package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Application {

    String inputFilename = "aoc2016/input07.txt";

    public static void main(String[] args) {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        Integer tlsSupport = 0;
        Integer sslSupport = 0;
        boolean ipSupportsTls;
        Matcher matcher;
        var patternMuss = Pattern.compile("([a-z])([a-z])\\2\\1");
        var patternDarfNicht = Pattern.compile(".*\\[[a-z]*([a-z])([a-z])\\2\\1[a-z]*].*");

        var patternPart2 = Pattern.compile("([a-z])([a-z])\\1.*#.*\\2\\1\\2");

        for (String line : lines) {
            if (!patternDarfNicht.matcher(line).find()) {
                ipSupportsTls = false;
                matcher = patternMuss.matcher(line);
                while (matcher.find()) {
                    ipSupportsTls = true;
                    if (Objects.equals(matcher.group(1), matcher.group(2))) {
                        ipSupportsTls = false;
                        break;
                    }
                }
                if (ipSupportsTls) {
                    tlsSupport++;
                }
            }

            // Part 2
            int spos = 0;
            int pos1, pos2;
            String[] blocks = new String[2];
            blocks[0] = "";
            blocks[1] = "";
            String blockKombination;
            do {
                pos1 = line.indexOf('[', spos);
                pos2 = line.indexOf(']', spos);
                if (pos1 == -1 || pos2 == -1) {
                    pos2 = line.length();
                    if (!blocks[0].isEmpty()) {
                        blocks[0] += "-";
                    }
                    blocks[0] += line.substring(spos, pos2);
                } else {
                    if (!blocks[0].isEmpty()) {
                        blocks[0] += "-";
                    }
                    blocks[0] += line.substring(spos, pos1);
                    if (!blocks[1].isEmpty()) {
                        blocks[1] += "-";
                    }
                    blocks[1] += line.substring(pos1 + 1, pos2);
                }
                spos = pos2 + 1;
            } while (spos < line.length());

            blockKombination = blocks[0] + "#" + blocks[1];
            var m2 = patternPart2.matcher(blockKombination);
            if (m2.find()) {
                if (!Objects.equals(m2.group(1), m2.group(2))) {
                    sslSupport++;
                }
            }
        }

        //        ([a-z])([a-z])\2\1.*\[|].*([a-z])([a-z])\4\3

        // muss:
        // ([a-z])([a-z])\2\1
        // darf nicht:
        // .*\[.*([a-z])([a-z])\2\1.*].*


        System.out.printf("Part 1 TLS support: %s\n", tlsSupport);
        System.out.printf("Part 2 SSL support: %s\n", sslSupport);
    }
}