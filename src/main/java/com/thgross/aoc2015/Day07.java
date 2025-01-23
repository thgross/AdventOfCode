package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Application {

    String inputFilename = "aoc2015/input07.txt";

    public static void main(String[] args) {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var vals = new HashMap<String, Integer>();

        var pSet = Pattern.compile("^(\\w+) -> ([a-z]+)$");   // lx -> er
        var pNOT = Pattern.compile("^NOT (\\w+) -> ([a-z]+)$");   // NOT lx -> er
        var pANDOR = Pattern.compile("^(\\w+) (AND|OR) (\\w+) -> ([a-z]+)$");  // as AND dw -> er
        var pSHIFT = Pattern.compile("^(\\w+) (LSHIFT|RSHIFT) (\\d+) -> ([a-z]+)$");  // as AND dw -> er
        for (String line : lines) {
            Matcher m;

            m = pSet.matcher(line);
            if (m.find()) {
                var src1 = m.group(1);
                Integer srcval1;
                var dest = m.group(2);

                if (src1.matches("\\d+")) {
                    srcval1 = Integer.parseInt(src1);
                } else {
                    if (!vals.containsKey(src1)) {
                        vals.put(src1, null);
                    }
                    srcval1 = vals.get(src1);
                }
                if (!vals.containsKey(dest)) {
                    vals.put(dest, null);
                }
                // TODO: calc
                continue;
            }

            m = pNOT.matcher(line);
            if (m.find()) {
                var wire1 = m.group(1);
                var wire2 = m.group(2);
                if (!vals.containsKey(wire1)) {
                    vals.put(wire1, null);
                }
                if (!vals.containsKey(wire2)) {
                    vals.put(wire2, null);
                }
                // TODO: calc
                continue;
            }

            m = pANDOR.matcher(line);
            if (m.find()) {
                var wire1 = m.group(1);
                var wire2 = m.group(2);
                var wire3 = m.group(3);
                if (!vals.containsKey(wire1)) {
                    vals.put(wire1, null);
                }
                if (!vals.containsKey(wire2)) {
                    vals.put(wire2, null);
                }
                if (!vals.containsKey(wire3)) {
                    vals.put(wire3, null);
                }
                // TODO: calc
                continue;
            }

            m = pSHIFT.matcher(line);
            if (m.find()) {
                var wire1 = m.group(1);
                var val = m.group(2);
                var wire2 = m.group(3);
                if (!vals.containsKey(wire1)) {
                    vals.put(wire1, null);
                }
                if (!vals.containsKey(wire2)) {
                    vals.put(wire2, null);
                }
                // TODO: calc
                continue;
            }

            throw new RuntimeException("Zeile konnte nicht geparst werden: " + line);
        }

        System.out.println(vals);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: lights on: %d\n", vals.get("a"));
    }
}
