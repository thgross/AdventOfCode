package com.thgross.aoc2015;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import java.io.IOException;
import java.util.List;

public class Day18 extends Application {

    public static final char OFF = '.';
    public static final char ON = '#';
//    String inputFilename = "aoc2015/input18-t1.txt";
//    int steps = 4;
    String inputFilename = "aoc2015/input18.txt";
    int steps = 100;

    public static void main(String[] args) {
        var app = (new Day18());
        app.run(app.inputFilename);
    }

    int mapW;
    int mapH;

    char[][][] map;
    int mapCurrent = 0;
    int mapPrevious = 1;

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        mapH = lines.size();
        mapW = lines.getFirst().length();
        map = new char[2][mapH][mapW];

        for (int i = 0; i < lines.size(); i++) {
            var lchars = lines.get(i).toCharArray();
            for (int i1 = 0; i1 < lchars.length; i1++) {
                map[mapCurrent][i][i1] = lchars[i1];
            }
        }

//        dumpMap(map[mapCurrent]);

        int part1LightsOn = 0;

        for (int i = 0; i < steps; i++) {
            mapCurrent = 1 - mapCurrent;
            mapPrevious = 1 - mapPrevious;
            part1LightsOn = stepMap(map[mapCurrent], map[mapPrevious]);
//            System.out.println();
//            dumpMap(map[mapCurrent]);
        }

        dumpMap(map[mapCurrent]);

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 Lights on after %d steps: %d\n", steps, part1LightsOn);
//        System.out.printf("Part 2 Ways with min Containers: %d\n", part2MinNumContainers.get(lowestContainerNum));
    }

    private int stepMap(char[][] mapTarget, char[][] mapSource) {
        int lightsOn = 0;
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
