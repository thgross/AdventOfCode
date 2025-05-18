package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day20 extends Application {

    String inputFilename = "aoc2015/input20.txt";

    public static void main(String[] args) {
        var app = (new Day20());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long presentsTarget = Long.parseLong(lines.getFirst());

        System.out.printf("Target: %,14d\n", presentsTarget);

        // Part 1
        long part1HouseNr = 0;

        long house;
        long presents;
        for (house = 832000; house > 831000; house--) {
            presents = 0;
                for (long elf = 1; elf <= house; elf++) {
                    if (house % elf == 0) {
                        presents += elf * 10;
                    }
                }
            if (/*house % 1000 == 0 || */presents >= presentsTarget) {
                part1HouseNr = house;
                System.out.printf("House %,8d: %,14d\n", house, presents);
            }
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | House Number: %d\n", part1HouseNr);
//        System.out.printf("Part 2 | Medicine created from 'e' after %d replacements: %d\n", 1, replacementCount);
    }
}
