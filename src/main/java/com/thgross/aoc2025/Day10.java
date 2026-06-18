package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10 extends Application {

    String inputFilename = "aoc2025/input10-t1.txt";

    static void main() {
        var app = (new Day10());
        app.run(app.inputFilename);
    }

    class Machine {
        short target = 0;
        short[] buttons;

        public String toString() {
            return target + " " + buttons + "\n";
        }
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int part1Presses = 0;

        var machines = new ArrayList<Machine>();

        for (String line : lines) {
            var m = new Machine();
            var tmpButtons = new ArrayList<Short>();
            var parts = line.split(" ");
            for (String part : parts) {
                if (part.startsWith("[")) {
                    // Target Lights
                    for (int c = 1; c < part.length() - 1; c++) {
                        if (part.charAt(c) == '#') {
                            m.target |= (short) (1 << c - 1);
                        }
                    }

                } else if (part.startsWith("(")) {
                    // Buttons
                    short btn = 0;
                    for (int c = 1; c < part.length() - 1; c += 2) {
                        short offset = Short.parseShort(String.valueOf(part.charAt(c)));
                        btn |= (short) (1 << offset);
                    }
                    tmpButtons.add(btn);

                } else if (part.startsWith("{")) {
                    // joltage Requirements
                }
                m.buttons = ArrayUtils.toPrimitive(tmpButtons.toArray(new Short[tmpButtons.size()]));
//                m.buttons = Arrays.stream(tmpButtons).mapToInt(Short::shortValue).toArray();
  //              m.buttons = Arrays.stream(tmpButtons).mapToInt(Short::shortValue).toArray();
            }
            machines.add(m);
        }

        for (Machine machine : machines) {
            System.out.printf(machine.toString());
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Presses: %d\n", part1Presses);
    }

}
