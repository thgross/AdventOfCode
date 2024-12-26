package com.thgross;

import com.thgross.aoc.Application;
import com.thgross.aoc.Pos;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AOC2024_15 extends Application {
    public static void main(String[] args) {

        var inputfile = "input15.txt";
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
    Pos tilesize2 = new Pos(16, 8);

    protected final char[] dirchars = {
            '^', '>', 'v', '<'
    };

    BufferedImage bi;
    Graphics2D g2d;

    Map<Character, Color> tileColors = new HashMap<>() {{
        put(WALL, new Color(0, 150, 0));
        put(FLOOR, new Color(50, 50, 255));
        put(BOX, new Color(160, 160, 0));
        put(BOX2L, new Color(160, 160, 0));
        put(BOX2R, new Color(160, 160, 0));
        put(ROBOT, new Color(255, 0, 0));
    }};

    private static class WandException extends Exception {
        public WandException(String message) {
            super(message);
        }
    }

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
        dumpMap(lc.map, null);
//        drawMap(lc.map, winframe.g);

        for (int i = 0; i < lc.robotRules.size(); i++) {
            lc.robotPos = moveCharOnMap(lc.map, lc.robotPos, lc.robotRules.get(i));
        }
        System.out.println("Final Map 1:");
        dumpMap(lc.map, null);
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
        dumpMap(lc.map2, null);
        bi = new BufferedImage(lc.map2W * tilesize2.x, lc.map2H * tilesize2.y, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) bi.getGraphics();
        saveMapImage(lc.map2, 0, bi, g2d, tilesize2);
//        drawMap(lc.map2, winframe.g);

        Pos oldRobotPos = lc.robot2Pos;
        for (int i = 0; i < lc.robotRules.size(); i++) {
            lc.robot2Pos = moveCharOnMap2(lc.map2, lc.robot2Pos, lc.robotRules.get(i));

            if (false) {
                System.out.printf("Move %d (%c)\n", i, dirchars[lc.robotRules.get(i)]);
                dumpMap(lc.map2, oldRobotPos);
                if (i < lc.robotRules.size() - 1) {
                    System.out.printf("%c\n", dirchars[lc.robotRules.get(i + 1)]);
                }
            }
            if (i > 174) {
//                System.out.println();
            }
            if (false) {
                saveMapImage(lc.map2, i + 1, bi, g2d, tilesize2);
                System.out.printf("image %d,", i + 1);
                if (i % 50 == 0) {
                    System.out.println();
                }
            }
            oldRobotPos.clone(lc.robot2Pos);
        }
        System.out.printf("Final Map 1 (%d Moves):\n", lc.robotRules.size());
        dumpMap(lc.map2, null);
        long sumOfCoordinates2 = calcSumOfBoxCoordinates(lc.map2);

        System.out.println("----------------------------------");
        System.out.printf("Part 1 sum of coordinates: %d\n", sumOfCoordinates);
        System.out.printf("Part 2 sum of coordinates: %d\n", sumOfCoordinates2);
    }

    void saveMapImage(char[][] map, int id, BufferedImage bi, Graphics2D g2d, Pos tilesize) {

        g2d.clearRect(0, 0, bi.getWidth(), bi.getHeight());

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                g2d.setColor(tileColors.get(map[y][x]));
//                g2d.fillRect(x * tilesize.x, y * tilesize.y, tilesize.x, tilesize.y);
                var tile = String.valueOf(map[y][x]);
                if (tile.equals("@")) {
                    tile = "$";
                }
                g2d.drawString(tile, x * tilesize.x, y * tilesize.y + tilesize.y);
            }
        }
        try {
            ImageIO.write(bi, "PNG", new File(String.format("_tmp/image-%06d.png", id)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private Pos moveCharOnMap2(char[][] map, Pos charpos, int direction) {
        // 1. find all Boxes in direction
        List<Pos> boxes;
        try {
            boxes = findBoxParts(map, charpos, direction);
            boxes = boxes.stream().distinct().collect(Collectors.toList());
            if (direction == TOP) {
                boxes.sort((a, b) -> Integer.compare(a.y, b.y));
            } else if (direction == BOTTOM) {
                boxes.sort((a, b) -> Integer.compare(b.y, a.y));
            } else if (direction == LEFT) {
                boxes.sort((a, b) -> Integer.compare(a.x, b.x));
            } else {
                boxes.sort((a, b) -> Integer.compare(b.x, a.x));
            }

            for (int i = 0; i < boxes.size(); i++) {
                var oldpos = boxes.get(i);
                var newpos = oldpos.plus(dirs[direction]);
                map[newpos.y][newpos.x] = map[oldpos.y][oldpos.x];
                map[oldpos.y][oldpos.x] = FLOOR;
            }
            var newCharPos = charpos.plus(dirs[direction]);
            map[newCharPos.y][newCharPos.x] = map[charpos.y][charpos.x];
            map[charpos.y][charpos.x] = FLOOR;

            return newCharPos;

        } catch (WandException exception) {
            // Mauer im Weg!
//            System.out.println(exception.getMessage());
            return charpos;
        }
    }

    private List<Pos> findBoxParts(char[][] map, Pos pos, int direction) throws WandException {
        var ret = new ArrayList<Pos>();
        var testpos = pos.plus(dirs[direction]);
        if (map[testpos.y][testpos.x] == BOX2L || map[testpos.y][testpos.x] == BOX2R) {
            if (map[testpos.y][testpos.x] == BOX2L) {
                ret.add(testpos);
                ret.add(testpos.plus(dirs[RIGHT]));
                if (direction == RIGHT) {
                    ret.addAll(findBoxParts(map, testpos.plus(dirs[RIGHT]), direction));
                } else {
                    ret.addAll(findBoxParts(map, testpos, direction));
                    if (direction != LEFT) {
                        ret.addAll(findBoxParts(map, testpos.plus(dirs[RIGHT]), direction));
                    }
                }
            } else {
                ret.add(testpos);
                ret.add(testpos.plus(dirs[LEFT]));
                if (direction == LEFT) {
                    ret.addAll(findBoxParts(map, testpos.plus(dirs[LEFT]), direction));
                } else {
                    ret.addAll(findBoxParts(map, testpos, direction));
                    if (direction != RIGHT) {
                        ret.addAll(findBoxParts(map, testpos.plus(dirs[LEFT]), direction));
                    }
                }
            }
        } else if (map[testpos.y][testpos.x] == WALL) {
            throw new WandException("Wand bei  " + pos.y + "/" + pos.x + " -> " + testpos.y + "/" + testpos.x + "!");
        }

        return ret;
    }

    private void dumpMap(char[][] map, Pos oldRobotPos) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                if (oldRobotPos != null && oldRobotPos.x == x && oldRobotPos.y == y) {
                    printChar(ROBOT, PURPLE);
                    continue;
                }

                printChar(map[y][x],
                        switch (map[y][x]) {
                            case WALL -> GREEN;
                            case FLOOR -> BLUE;
                            case BOX -> YELLOW;
                            case BOX2L -> YELLOW;
                            case BOX2R -> YELLOW;
                            case ROBOT -> RED;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        },
                        switch (map[y][x]) {
                            case WALL -> BG_BLACK;
                            case FLOOR -> BG_BLACK;
                            case BOX -> BG_BLACK;
                            case BOX2L -> BG_BLACK;
                            case BOX2R -> BG_BLACK;
                            case ROBOT -> BG_BLACK;
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
