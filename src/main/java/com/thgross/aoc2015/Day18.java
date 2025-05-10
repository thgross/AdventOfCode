package com.thgross.aoc2015;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.List;

public class Day18 extends Application {

    public static final char OFF = '.';
    public static final char ON = '#';
//    String inputFilename = "aoc2015/input18-t1.txt";
//    int steps = 5;
    String inputFilename = "aoc2015/input18.txt";
    int steps = 100;

    public static void main(String[] args) {
        var app = (new Day18());
        app.run(app.inputFilename);
    }

    int mapW;
    int mapH;

    char[][][] map;
    char[][][] map2;
    int mapCurrent = 0;
    int mapPrevious = 1;

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        mapH = lines.size();
        mapW = lines.getFirst().length();
        map = new char[2][mapH][mapW];
        map2 = new char[2][mapH][mapW];

        for (int i = 0; i < lines.size(); i++) {
            var lchars = lines.get(i).toCharArray();
            for (int i1 = 0; i1 < lchars.length; i1++) {
                map[mapCurrent][i][i1] = lchars[i1];
                map2[mapCurrent][i][i1] = lchars[i1];
            }
        }

//        dumpMap(map[mapCurrent]);


        // Part 1
        int part1LightsOn = 0;
        mapCurrent = 0;
        mapPrevious = 1;
        for (int i = 0; i < steps; i++) {
            mapCurrent = 1 - mapCurrent;
            mapPrevious = 1 - mapPrevious;
            part1LightsOn = stepMap(map[mapCurrent], map[mapPrevious], false);
        }
        System.out.printf("Map 1 after %d steps:\n", steps);
        dumpMap(map[mapCurrent]);

        // Part 2
        int part2LightsOn = 0;
        mapCurrent = 0;
        mapPrevious = 1;
        System.out.print("Map 2 initial:\n");
        dumpMap(map2[mapCurrent]);
        for (int i = 0; i < steps; i++) {
            mapCurrent = 1 - mapCurrent;
            mapPrevious = 1 - mapPrevious;
            part2LightsOn = stepMap(map2[mapCurrent], map2[mapPrevious], true);
//            System.out.printf("Map 2 after %d steps:\n", i + 1);
//            dumpMap(map2[mapCurrent]);
        }
        System.out.printf("Map 2 after %d steps:\n", steps);
        dumpMap(map2[mapCurrent]);


        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 Lights on after %d steps: %d\n", steps, part1LightsOn);
        System.out.printf("Part 2 Lights on after %d steps: %d\n", steps, part2LightsOn);
//        System.out.printf("Part 2 Ways with min Containers: %d\n", part2MinNumContainers.get(lowestContainerNum));
    }

    private int stepMap(char[][] mapTarget, char[][] mapSource, boolean fixedCorners) {
        int lightsOn = 0;
        if (fixedCorners) {
            mapSource[0][0] = ON;
            mapSource[0][mapW - 1] = ON;
            mapSource[mapH - 1][0] = ON;
            mapSource[mapH - 1][mapW - 1] = ON;
        }
        for (int y = 0; y < mapSource.length; y++) {
            for (int x = 0; x < mapSource[y].length; x++) {
                int aroundOn = 0;
                for (Pos offset : Pos.around) {
                    if (y + offset.y >= 0 && y + offset.y < mapSource.length && x + offset.x >= 0 && x + offset.x < mapSource[y].length) {
                        if (mapSource[y + offset.y][x + offset.x] == ON) {
                            aroundOn++;
                        }
                    }
                }
                if (mapSource[y][x] == ON) {
                    if (aroundOn >= 2 && aroundOn <= 3) {
                        mapTarget[y][x] = ON;
                        lightsOn++;
                    } else {
                        mapTarget[y][x] = OFF;
                    }
                } else {
                    if (aroundOn == 3) {
                        mapTarget[y][x] = ON;
                        lightsOn++;
                    } else {
                        mapTarget[y][x] = OFF;
                    }
                }
            }
        }

        if (fixedCorners) {
            lightsOn += mapTarget[0][0] != ON ? 1 : 0;
            lightsOn += mapTarget[0][mapW - 1] != ON ? 1 : 0;
            lightsOn += mapTarget[mapH - 1][0] != ON ? 1 : 0;
            lightsOn += mapTarget[mapH - 1][mapW - 1] != ON ? 1 : 0;
            mapTarget[0][0] = ON;
            mapTarget[0][mapW - 1] = ON;
            mapTarget[mapH - 1][0] = ON;
            mapTarget[mapH - 1][mapW - 1] = ON;
        }

        return lightsOn;
    }

    private void dumpMap(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                printChar(map[y][x],
                        switch (map[y][x]) {
                            case ON -> GREEN;
                            case OFF -> BLUE;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        });
            }
            System.out.println();
        }
    }
}
