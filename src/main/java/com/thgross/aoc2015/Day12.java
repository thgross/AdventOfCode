package com.thgross.aoc2015;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day12 extends Application {

    String inputFilename = "aoc2015/input12.json";

    Pattern numbers = Pattern.compile("(-?\\d+)");

    public static void main(String[] args) {
        var app = (new Day12());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int val1 = 0;
        var jsonString = lines.getFirst();

        var matcher = numbers.matcher(jsonString);
        while (matcher.find()) {
            for (int gr = 0; gr < matcher.groupCount(); gr++) {
                var mg = matcher.group(gr);
                val1 += Integer.parseInt(mg);
            }
        }

        var gson = new Gson();
        var jsonObject = gson.fromJson(jsonString, Object.class);

        Long val2 = countObject(jsonObject);

        System.out.println("--------------------------------------");
        System.out.printf("Part 1: Sum: %d\n", val1);
        System.out.printf("Part 2: Sum: %d\n", val2);
    }

    private Long countObject(Object o) {

        if (o instanceof LinkedTreeMap<?, ?> map) {
            if (map.containsValue("red")) {
                return 0L;
            }
            Long sum = 0L;
            for (Object key : map.keySet()) {
                sum += countObject(map.get(key));
            }
            return sum;
        } else if (o instanceof ArrayList<?> list) {
            Long sum = 0L;
            for (Object object : list) {
                sum += countObject(object);
            }
            return sum;
        } else if (o instanceof Double num) {
            return num.longValue();
        } else if (o instanceof String s) {
            // do nothing
        } else {
            throw new RuntimeException("unbekannter Typ");
        }

        return 0L;
    }
}
