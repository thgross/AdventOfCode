package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.regex.Pattern;

public class AOC2024_04 extends Application {
    public static void main(String[] args) {
        (new AOC2024_04()).run();
    }

    @Override
    public void run() {
        try {
            calcAll("aoc2024/input04.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        int count = 0;
        int count2 = 0;

        var content = getFileContent(inputFile);

        // Teil 1
        Pattern[] patterns = new Pattern[]{
                Pattern.compile("(?=(XMAS|SAMX))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(X.{139}M.{139}A.{139}S))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(X.{140}M.{140}A.{140}S))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(X.{141}M.{141}A.{141}S))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(S.{139}A.{139}M.{139}X))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(S.{140}A.{140}M.{140}X))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(S.{141}A.{141}M.{141}X))", Pattern.DOTALL | Pattern.MULTILINE)
        };

        for (Pattern pattern : patterns) {
            var matcher = pattern.matcher(content);

            while(matcher.find()) {
                count += matcher.groupCount();
            }
        }

        // Teil 2
        Pattern[] patterns2 = new Pattern[]{
                Pattern.compile("(?=(M.M.{139}A.{139}S.S))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(S.M.{139}A.{139}S.M))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(M.S.{139}A.{139}M.S))", Pattern.DOTALL | Pattern.MULTILINE),
                Pattern.compile("(?=(S.S.{139}A.{139}M.M))", Pattern.DOTALL | Pattern.MULTILINE)
        };

        for (Pattern pattern : patterns2) {
            var matcher = pattern.matcher(content);

            while(matcher.find()) {
                count2 += matcher.groupCount();
            }
        }

        System.out.printf("Matches: %d\n", count);
        System.out.printf("Matches 2: %d\n", count2);
    }
}
