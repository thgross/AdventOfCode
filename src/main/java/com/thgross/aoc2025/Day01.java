package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day01 extends Application {

    static final Integer DIAL_SIZE = 100;
    static final Integer START_POS = 50;

    String inputFilename = "aoc2025/input01-t1.txt";

    public static void main(String[] args) {
        var app = (new Day01());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int dialPositionOld = START_POS;
        int dialPosition = START_POS;
        int passwordNumberPart1 = 0;
        int passwordNumberPart2 = 0;
        int direction;
        int distance;

        for (String line : lines) {
            direction = line.charAt(0) == 'L' ? -1 : 1;
            distance = Integer.parseInt(line.substring(1));

            dialPosition += distance * direction;
            dialPosition = Math.floorMod(dialPosition, DIAL_SIZE);

            if (dialPosition == 0) {
                passwordNumberPart1++;
                passwordNumberPart2++;
            }

            if (dialPosition > dialPositionOld && direction == -1 && dialPosition != 0) {
                passwordNumberPart2++;
            }
            if (dialPosition < dialPositionOld && direction == 1 && dialPosition != 0) {
                passwordNumberPart2++;
            }

            dialPositionOld = dialPosition;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Password: %d\n", passwordNumberPart1);
        System.out.printf("Part 2 Password: %d\n", passwordNumberPart2);
    }
}
