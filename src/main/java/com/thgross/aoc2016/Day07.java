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
        boolean ipSupportsTls;
        Matcher matcher;
        var patternMuss = Pattern.compile("([a-z])([a-z])\\2\\1");
        var patternDarfNicht = Pattern.compile(".*\\[[a-z]*([a-z])([a-z])\\2\\1[a-z]*].*");

        for (String line : lines) {
            if(!patternDarfNicht.matcher(line).find()) {
                ipSupportsTls = false;
                matcher = patternMuss.matcher(line);
                while(matcher.find()) {
                    ipSupportsTls = true;
                    if(Objects.equals(matcher.group(1), matcher.group(2))) {
                        ipSupportsTls = false;
                        break;
                    }
                }
                if(ipSupportsTls) {
                    tlsSupport++;
                }
            }
        }

    //        ([a-z])([a-z])\2\1.*\[|].*([a-z])([a-z])\4\3

        // muss:
        // ([a-z])([a-z])\2\1
        // darf nicht:
        // .*\[.*([a-z])([a-z])\2\1.*].*


       System.out.printf("Part 1 TLS support: %s\n", tlsSupport);
//        System.out.printf("Part 2 Message: %s\n", p2Message);
    }
}