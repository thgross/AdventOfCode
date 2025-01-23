package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Application {

    String inputFilename = "aoc2015/input07.txt";

    public static void main(String[] args) {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    Map<String, Integer> vals;

    protected Integer getval(String src) {
        if (src.matches("\\d+")) {
            return Integer.parseInt(src);
        } else {
            if (!vals.containsKey(src)) {
                vals.put(src, null);
            }
            return vals.get(src);
        }
    }

    protected void putval(String dest, Integer val) {
        vals.put(dest, val);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        vals = new HashMap<>();

        var pSet = Pattern.compile("^(\\w+) -> ([a-z]+)$");   // lx -> er
        var pNOT = Pattern.compile("^NOT (\\w+) -> ([a-z]+)$");   // NOT lx -> er
        var pANDOR = Pattern.compile("^(\\w+) (AND|OR) (\\w+) -> ([a-z]+)$");  // as AND dw -> er
        var pSHIFT = Pattern.compile("^(\\w+) (LSHIFT|RSHIFT) (\\d+) -> ([a-z]+)$");  // as AND dw -> er
        for (String line : lines) {
            Matcher m;

            m = pSet.matcher(line);
            if (m.find()) {
                var wire1Val = getval(m.group(1));
                var destWire = m.group(2);

                if(wire1Val == null) {
                    continue;
                }

                // Calc
                putval(destWire, wire1Val);

                continue;
            }

            m = pNOT.matcher(line);
            if (m.find()) {
                var wire1Val = getval(m.group(1));
                var destWire = m.group(2);

                if(wire1Val == null) {
                    continue;
                }

                // Calc
                putval(destWire, (~wire1Val) & 0xFFFF);

                continue;
            }

            m = pANDOR.matcher(line);
            if (m.find()) {
                var wire1Val = getval(m.group(1));
                var operation = m.group(2);
                var wire2Val = getval(m.group(3));
                var destWire = m.group(4);

                if(wire1Val == null || wire2Val == null) {
                    continue;
                }

                // Calc
                if (operation.equals("AND")) {
                    putval(destWire, (wire1Val & wire2Val) & 0xFFFF);
                } else if (operation.equals("OR")) {
                    putval(destWire, (wire1Val | wire2Val) & 0xFFFF);
                }

                continue;
            }

            m = pSHIFT.matcher(line);
            if (m.find()) {
                var wire1Val = getval(m.group(1));
                var operation = m.group(2);
                var wire2Val = getval(m.group(3));
                var destWire = m.group(4);

                if(wire1Val == null || wire2Val == null) {
                    continue;
                }

                // Calc
                if (operation.equals("LSHIFT")) {
                    putval(destWire, (wire1Val << wire2Val) & 0xFFFF);
                } else if (operation.equals("RSHIFT")) {
                    putval(destWire, (wire1Val >> wire2Val) & 0xFFFF);
                }
                continue;
            }

            throw new RuntimeException("Zeile konnte nicht geparst werden: " + line);
        }

        System.out.println(vals);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: lights on: %d\n", vals.get("a"));
    }
}
