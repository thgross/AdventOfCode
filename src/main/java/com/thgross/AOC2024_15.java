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
    static final char ROBOT = '@';

    static class Pdata {
        int mapW, mapH;
        char[][] map;
        Pos robotPos = new Pos();
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

        openWindow("AOC 2024 Day 15", lc.mapW * tilesize.x, lc.mapH * tilesize.y);

        System.out.println("Initial Map:");
        dumpMap(lc.map);
        drawMap(lc.map, winframe.g);

        // Teil 1
        for (int i = 0; i < lc.robotRules.size(); i++) {
            lc.robotPos = moveCharOnMap(lc.map, lc.robotPos, lc.robotRules.get(i));
        }
        System.out.println("Final Map:");
        dumpMap(lc.map);

        long sumOfCoordinates = calcSumOfBoxCoordinates(lc.map);

        System.out.println("------------------------------");
        System.out.printf("sum of coordinates: %d\n", sumOfCoordinates);
    }

    long calcSumOfBoxCoordinates(char[][] map) {
        long sum = 0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == BOX) {
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

                printChar(map[y][x], switch (map[y][x]) {
                    case WALL -> ANSI_GREEN;
                    case FLOOR -> ANSI_BLUE;
                    case BOX -> ANSI_YELLOW;
                    case ROBOT -> ANSI_RED;
                    default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                });
            }
            System.out.println();
        }
    }

    private void drawMap(char[][] map, Graphics2D g) {
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
