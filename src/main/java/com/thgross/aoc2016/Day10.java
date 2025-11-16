package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 extends Application {

    String inputFilename = "aoc2016/input10-t1.txt";

    Pattern patternBotGives = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");
    Pattern patternValueGoes = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    Matcher matcherBot, matcherValue;

    private interface Target {
        public void putValue(Integer val);
    }

    private final class Bot implements Target {
        public List<Integer> chips = new ArrayList<>();
        public Map<Integer, Target> rule = new HashMap<>();

        @Override
        public void putValue(Integer val) {
            chips.add(val);
        }
    }

    private final class Output implements Target {
        public Integer value;

        @Override
        public void putValue(Integer val) {
            value = val;
        }
    }

    private final class Pool<T extends Target> {

        private final Class<T> targetClass;
        public Map<Integer, T> objects = new HashMap<>();

        public Pool(Class<T> tClass) {
            this.targetClass = tClass;
        }

        public T get(Integer index) throws Exception {
            if (objects.containsKey(index)) {
                return objects.get(index);
            } else {
                return objects.put(index, targetClass.getDeclaredConstructor().newInstance());
            }
        }
    }

    public static void main(String[] args) {
        var app = (new Day10());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) {

        var bots = new Pool<>(Bot.class);
        var outputs = new Pool<>(Output.class);

        for (String line : lines) {
            matcherBot = patternBotGives.matcher(line);
            if (matcherBot.find()) {

                var botFrom = bots.get(Integer.parseInt(matcherBot.group(1)));

                var poolLowTo = matcherBot.group(2) == "bot" ? bots : outputs;
                var poolHighTo = matcherBot.group(4) == "bot" ? bots : outputs;

                var lowTo = poolLowTo.get(Integer.parseInt(matcherBot.group(3)));
                var highTo = poolHighTo.get(Integer.parseInt(matcherBot.group(4)));

            }
        }
    }
}