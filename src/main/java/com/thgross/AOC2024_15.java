package com.thgross;

import com.thgross.aoc.Application;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AOC2024_15 extends Application {
    public static void main(String[] args) {

        var inputfile = "input15-t1.txt";
        var app = (new AOC2024_15());
//        app.openWindow(inputfile, 1000, 1000);
        app.run(inputfile);
    }

    static final char WALL = '#';
    static final char FLOOR = '.';
    static final char BOX = 'O';
    static final char BOX2L = '[';
    static final char BOX2R = ']';
    static final char ROBOT = '@';

    static class Pdata {
        int mapW, mapH;
        int map2W, map2H;
        char[][] map;
        char[][] map2;
        Pos robotPos = new Pos();
        Pos robot2Pos = new Pos();
        List<Integer> robotRules = new ArrayList<>();
    }

    private final AOC2024_15.Pdata lc = new AOC2024_15.Pdata();

    Pos tilesize = new Pos(16, 16);

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> slines) throws IOException {

        var maplines = new ArrayList<String>();
        for (String sline : slines) {
            if (sline.startsWith("#")) {
                maplines.add(sline);
            } else if (!sline.isEmpty()) {
                for (int i = 0; i < sline.length(); i++) {
                    lc.robotRules.add(switch (sline.charAt(i)) {
                        case '^' -> 0;
                        case '>' -> 1;
                        case 'v' -> 2;
                        case '<' -> 3;
                        default -> throw new RuntimeException("unbekannte Richtungsanweisung " + sline.charAt(i));
                    });
                }
            }
        }

//        openWindow("AOC 2024 Day 15", lc.mapW * tilesize.x, lc.mapH * tilesize.y);

        // Part 1
        // parse map
        lc.mapW = maplines.getFirst().length();
        lc.mapH = maplines.size();
        lc.map = new char[lc.mapH][lc.mapW];
        for (int y = 0; y < maplines.size(); y++) {
            var mapline = maplines.get(y);
            for (int x = 0; x < mapline.length(); x++) {
                lc.map[y][x] = mapline.charAt(x);
                if (lc.map[y][x] == ROBOT) {
                    lc.robotPos.x = x;
                    lc.robotPos.y = y;
                }
            }
        }

        System.out.println("Initial Map 1:");
        dumpMap(lc.map);
//        drawMap(lc.map, winframe.g);

        for (int i = 0; i < lc.robotRules.size(); i++) {
            lc.robotPos = moveCharOnMap(lc.map, lc.robotPos, lc.robotRules.get(i));
        }
        System.out.println("Final Map 1:");
        dumpMap(lc.map);
        long sumOfCoordinates = calcSumOfBoxCoordinates(lc.map);

        // Part 2
        // parse map
        lc.map2W = maplines.getFirst().length() * 2;
        lc.map2H = maplines.size();
        lc.map2 = new char[lc.map2H][lc.map2W];
        for (int y = 0; y < maplines.size(); y++) {
            var mapline = maplines.get(y);
            for (int x = 0; x < mapline.length(); x++) {
                char tile = mapline.charAt(x);
                switch (tile) {
                    case '#':
                        lc.map2[y][x * 2] = tile;
                        lc.map2[y][x * 2 + 1] = tile;
                        break;
                    case 'O':
                        lc.map2[y][x * 2] = BOX2L;
                        lc.map2[y][x * 2 + 1] = BOX2R;
                        break;
                    case '.':
                        lc.map2[y][x * 2] = FLOOR;
                        lc.map2[y][x * 2 + 1] = FLOOR;
                        break;
                    case '@':
                        lc.map2[y][x * 2] = ROBOT;
                        lc.map2[y][x * 2 + 1] = FLOOR;
                        break;
                }
                if (lc.map2[y][x * 2] == ROBOT) {
                    lc.robot2Pos.x = x * 2;
                    lc.robot2Pos.y = y;
                }
            }
        }
        System.out.println("Initial Map 2:");
        dumpMap(lc.map2);
//        drawMap(lc.map2, winframe.g);
        long sumOfCoordinates2 = calcSumOfBoxCoordinates(lc.map2);


        System.out.println("----------------------------------");
        System.out.printf("Part 1 sum of coordinates: %d\n", sumOfCoordinates);
        System.out.printf("Part 2 sum of coordinates: %d\n", sumOfCoordinates2);
    }

    long calcSumOfBoxCoordinates(char[][] map) {
        long sum = 0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == BOX || map[y][x] == BOX2L) {
                    sum += y * 100L + x;
                }
            }
        }

        return sum;
    }

    private Pos moveCharOnMap(char[][] map, Pos charpos, int direction) {
        // 1. find empty floor in direction
        var freepos = new Pos(charpos.y, charpos.x);
        do {
            freepos.add(dirs[direction]);
            if (map[freepos.y][freepos.x] == WALL) {
                freepos = null;
                break;
            } else if (map[freepos.y][freepos.x] == FLOOR) {
                break;
            }
        } while (true);

        if (freepos == null) {
            return charpos;
        }

        // movement possible
        do {
            var shiftPos = freepos.minus(dirs[direction]);
            map[freepos.y][freepos.x] = map[shiftPos.y][shiftPos.x];
            map[shiftPos.y][shiftPos.x] = FLOOR;
            freepos = shiftPos;
        } while (!freepos.isEqualTo(charpos));

        return freepos.plus(dirs[direction]);
    }

    private void dumpMap(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                printChar(map[y][x],
                        switch (map[y][x]) {
                            case WALL -> ANSI_GREEN;
                            case FLOOR -> ANSI_BLUE;
                            case BOX -> ANSI_YELLOW;
                            case BOX2L -> ANSI_YELLOW;
                            case BOX2R -> ANSI_YELLOW;
                            case ROBOT -> ANSI_RED;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        },
                        switch (map[y][x]) {
                            case WALL -> ANSI_BLACK_BACKGROUND;
                            case FLOOR -> ANSI_BLACK_BACKGROUND;
                            case BOX -> ANSI_BLACK_BACKGROUND;
                            case BOX2L -> ANSI_BLACK_BACKGROUND;
                            case BOX2R -> ANSI_BLACK_BACKGROUND;
                            case ROBOT -> ANSI_BLACK_BACKGROUND;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        });
            }
            System.out.println();
        }
    }

    private void drawMap(char[][] map, Graphics2D g) {
        if (g == null) {
            return;
        }
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                g.setColor(switch (map[y][x]) {
                    case WALL -> Color.GREEN;
                    case FLOOR -> Color.BLACK;
                    case BOX -> Color.YELLOW;
                    case ROBOT -> Color.RED;
                    default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                });
                g.fillRect(x * tilesize.x, y * tilesize.y, tilesize.x, tilesize.y);
            }
        }
    }
}
