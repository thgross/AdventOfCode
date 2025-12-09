package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;

public class Day01 extends Application {

    static final Integer DIAL_SIZE = 100;
    static final Integer START_POS = 50;

    String inputFilename = "aoc2025/input01.txt";

    public static void main(String[] args) {
        var app = (new Day01());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int dialPositionOld = START_POS;
        int dialPosition = START_POS;
        int dialPositionModulo = START_POS;
        int dialPositionModuloOld = START_POS;
        int passwordNumberPart1 = 0;
        int passwordNumberPart2 = 0;
        int direction;
        int distance, dirDistance;

        for (String line : lines) {
            direction = line.charAt(0) == 'L' ? -1 : 1;
            distance = Integer.parseInt(line.substring(1));
            dirDistance = distance * direction;

            dialPosition += dirDistance;
            dialPositionModulo = Math.floorMod(dialPosition, DIAL_SIZE);

            if (dialPositionModulo == 0) {
                passwordNumberPart1++;
            }

            // TODO: Berechen, wie oft die angegebenen Rotationen (links oder rechts mit Anzahl Klicks) an der 0
            //  vorbeikommen

            var laufzeit = dialPositionModuloOld + dirDistance;
            passwordNumberPart2 += laufzeit / DIAL_SIZE;

            if (direction == -1) {
//                passwordNumberPart2 += (dialPositionModulo + distance) / DIAL_SIZE;
            } else {
//                passwordNumberPart2 += (dialPositionModuloOld + distance) / DIAL_SIZE;
            }

//            System.out.printf("%4d %s %4d = %4d  (%4d mod) (pn1: %d pn2: %d)\n", dialPositionOld, direction == -1 ? "<-" : "->", distance, dialPosition, dialPositionModulo, passwordNumberPart1, passwordNumberPart2);

            dialPositionOld = dialPosition;
            dialPositionModuloOld = dialPositionModulo;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1 Password: %d\n", passwordNumberPart1);
        System.out.printf("Part 2 Password: %d\n", passwordNumberPart2);
    }

}
