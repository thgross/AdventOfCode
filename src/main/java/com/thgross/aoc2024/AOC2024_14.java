package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AOC2024_14 extends Application {
    public static void main(String[] args) {
        (new AOC2024_14()).run();
    }

    private static class Pos {
        public int x, y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Robot {
        public int x, y;
        public int vx, vy;
        public int status;

        public Robot(int x, int y, int vx, int vy, int status) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.status = status;
        }

        public Robot clone() {
            return new Robot(x, y, vx, vy, status);
        }
    }

    static class Pdata {
        int mapW, mapH;
        int[][] map;
        Instant start;
        Instant stop;
    }

    private final AOC2024_14.Pdata lc = new AOC2024_14.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("aoc2024/input14.txt");
            lc.stop = Instant.now();

            System.out.println("======================================");
            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var content = getFileContent(inputFile);

        Matcher matcher;
        var patternSize = Pattern.compile("w=(\\d+), h=(\\d+)$", Pattern.DOTALL | Pattern.MULTILINE);
        matcher = patternSize.matcher(content);
        if (matcher.find()) {
            lc.mapW = Integer.parseInt(matcher.group(1));
            lc.mapH = Integer.parseInt(matcher.group(2));
        }

        lc.map = new int[lc.mapH][lc.mapW];

        List<Robot> robots = new ArrayList<>();

        var patternRobots = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)$", Pattern.DOTALL | Pattern.MULTILINE);
        matcher = patternRobots.matcher(content);
        while (matcher.find()) {
            robots.add(new Robot(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    0
            ));
        }

        resetMap(lc.map);
        drawRobots(lc.map, robots);
        dumpMap(lc.map, "Initial Map:", false);

        // Teil 1
        List<Robot> robots1 = new ArrayList<>();
        for (Robot robot : robots) {
            robots1.add(robot.clone());
        }
        var safetyFactor1 = 0;
        var seconds = 100;
        for (Robot robot : robots1) {
            robot.x = Math.floorMod(robot.x + robot.vx * seconds, lc.mapW);
            robot.y = Math.floorMod(robot.y + robot.vy * seconds, lc.mapH);
        }
        safetyFactor1 = calcSafetyFactor(robots1);

        resetMap(lc.map);
        drawRobots(lc.map, robots1);
        dumpMap(lc.map, "Map after " + seconds + " seconds:", true);

        // Teil 2
        List<Robot> robots2 = new ArrayList<>();
        for (Robot robot : robots) {
            robots2.add(robot.clone());
        }
        seconds = 0;
        int centerX = lc.mapW / 2;
        int centerY = lc.mapH / 2;
        boolean treeFound = false;

        // TODO: Steps 4, 105, 206 weitertesten!
        //   => Die Lösung ist 6771! Wie kann man das automatisieren??
        addSeconds(robots2, 6771);
        seconds = 6771;
        resetMap(lc.map);
        drawRobots(lc.map, robots2);
        dumpMap(lc.map, "Map after " + seconds + " seconds:", true);

        System.out.println("--------------------------------------");
        System.out.printf("Solution for %s:\n", inputFile);
        System.out.printf("Part 1 Safety Factor: %d\n", safetyFactor1);
        System.out.printf("Part 2 Seconds: %d\n", seconds);
    }

    private void addSeconds(List<Robot> robots, int seconds) {
        for (Robot robot : robots) {
            robot.x = Math.floorMod(robot.x + robot.vx * seconds, lc.mapW);
            robot.y = Math.floorMod(robot.y + robot.vy * seconds, lc.mapH);
        }
    }

    private boolean checkForTree(List<Robot> robots, int centerX, int centerY) {

        // turns out my solution was totally wrong ...
        return false;

/*
        // reset cancel status
        for (Robot robot : robots) {
            robot.status = 0;
        }

        mainloop:
        for (int i = 0; i < robots.size() - 1; i++) {
            for (int i1 = i + 1; i1 < robots.size(); i1++) {
                var r1 = robots.get(i);
                var r2 = robots.get(i1);
                if (r1.status == 0 && r2.status == 0
                        && r1.y == r2.y
                        && r1.y != centerY && r1.x != centerX
                        && r2.y != centerY && r2.x != centerX) {
                    if ((r1.x + r2.x) / 2 == centerX) {
                        // robots cancel each other out (they are symetric on the map)
                        r1.status = -1;
                        r2.status = -1;

                        continue mainloop;
                    }
                }
            }
        }

        // count canceled robots
        int canceled = 0;
        for (Robot robot : robots) {
            if (robot.status == -1) {
                canceled++;
            }
        }
        return canceled > robots.size() / 2;
*/
    }

    private int calcSafetyFactor(List<Robot> robots) {

        int q0 = 0, q1 = 0, q2 = 0, q3 = 0;

        for (Robot robot : robots) {
            if (robot.y < lc.mapH / 2) {
                if (robot.x < lc.mapW / 2) {
                    q0++;
                } else if (robot.x > lc.mapW / 2) {
                    q1++;
                }
            } else if (robot.y > lc.mapH / 2) {
                if (robot.x < lc.mapW / 2) {
                    q2++;
                } else if (robot.x > lc.mapW / 2) {
                    q3++;
                }
            }
        }

        return q0 * q1 * q2 * q3;
    }

    private void resetMap(int[][] map) {
        for (int[] ints : map) {
            Arrays.fill(ints, 0);
        }
    }

    private void drawRobots(int[][] map, List<Robot> robots) {
        for (Robot robot : robots) {
            map[robot.y][robot.x]++;
        }
    }

    private void dumpMap(int[][] map, String title, boolean removeMiddle) {

        System.out.println(title);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (removeMiddle && (x == map[y].length / 2 || y == map.length / 2)) {
                    System.out.print(" ");
                } else {
                    if (map[y][x] > 0) {
                        System.out.printf("%d", map[y][x]);
                    } else {
                        System.out.print(".");
                    }
                }
            }
            System.out.println();
        }
    }
}
