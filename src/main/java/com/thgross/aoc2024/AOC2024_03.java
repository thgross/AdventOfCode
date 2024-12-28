package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.regex.Pattern;

public class AOC2024_03 extends Application {
    public static void main(String[] args) {
        (new AOC2024_03()).run();
    }

    @Override
    public void run() {
        try {
            calcAll("aoc2024/input03.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        int sum = 0;
        int sumWithDos = 0;

        var content = getFileContent(inputFile);

        // Teil 1
        var matcher = Pattern.compile("mul\\((\\d+),(\\d+)\\)")
                .matcher(content);

        while (matcher.find()) {
            sum += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }

        // Teil 2
        var matcher2 = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)")
                .matcher(content);

        boolean doit = true;
        while (matcher2.find()) {
            if (matcher2.group(0).equals("do()")) {
                doit = true;
            } else if (matcher2.group(0).equals("don't()")) {
                doit = false;
            } else {
                if (doit) {
                    sumWithDos += Integer.parseInt(matcher2.group(1)) * Integer.parseInt(matcher2.group(2));
                }
            }
        }

        System.out.printf("Mutliplication Result: %d\n", sum);
        System.out.printf("Mutliplication Result with do() / don't(): %d\n", sumWithDos);
    }
}
