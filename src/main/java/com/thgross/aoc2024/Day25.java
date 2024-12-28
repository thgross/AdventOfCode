package com.thgross.aoc2024;

import com.thgross.aoc.Application;
import com.thgross.aoc2024.day25.Heightmap;
import com.thgross.aoc2024.day25.Key;
import com.thgross.aoc2024.day25.Lock;

import java.io.IOException;
import java.util.*;

public class Day25 extends Application {

    String inputFilename = "aoc2024/input25.txt";

    public static void main(String[] args) {
        var app = (new Day25());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<Lock> locks = new ArrayList<>();
        List<Key> keys = new ArrayList<>();

        // Heightmap lines
        List<String> hmlines = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                var hm = Heightmap.fromHMLines(hmlines);
                if (hm instanceof Lock) {
                    locks.add((Lock) hm);
                } else {
                    keys.add((Key) hm);
                }
                hmlines.clear();
            } else {
                hmlines.add(line);
            }
        }
        var hm = Heightmap.fromHMLines(hmlines);
        if (hm instanceof Lock) {
            locks.add((Lock) hm);
        } else {
            keys.add((Key) hm);
        }

        // Locks
        System.out.println("Locks:");
        System.out.println(locks);
        System.out.println("Keys:");
        System.out.println(keys);

        // Part 1
        long pairsFitting = 0;
        for (Lock lock : locks) {
            for (Key key : keys) {
                if (lock.fits(key)) {
                    pairsFitting++;
                }
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Lock/Keys pairs that fit: %d\n", pairsFitting);
    }
}
