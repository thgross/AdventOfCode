package com.thgross;

import com.thgross.aoc.Application;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AOC2024_16 extends Application {
    public static void main(String[] args) {
        var inputfile = "input16-t-tgtest1.txt";
        var app = (new AOC2024_16());
        app.run(inputfile);
    }

    //    static final char WALL = '■';
//    static final char WALL = '#';
    static final char WALL = '▓';
    static final char FLOOR = '·';
    static final char START = 'S';
    static final char END = 'E';
    static final char CUP = '^';
    static final char CRIGHT = '>';
    static final char CDOWN = 'v';
    static final char WASPATH = ':';
    static final char CLEFT = '<';

    static class Pdata {
        int mapW, mapH;
        char[][] map;
        Pos reindeerPos = new Pos();
        Pos endPos = new Pos();
        int rdDir;
    }

    protected static class Results {
        int minScore;
        int currentScore;
        long iterations;
    }

    private final AOC2024_16.Pdata lc = new AOC2024_16.Pdata();

    Pos tilesize = new Pos(6, 6);

    char[] dirChars = {
            CUP, CRIGHT, CDOWN, CLEFT
    };

    Map<Character, Color> tileColors = new HashMap<>() {{
        put(WALL, new Color(0, 100, 0));
        put(FLOOR, new Color(160, 160, 255));
        put(CUP, new Color(250, 250, 0));
        put(CRIGHT, new Color(250, 250, 0));
        put(CDOWN, new Color(250, 250, 0));
        put(CLEFT, new Color(250, 250, 0));
        put(WASPATH, new Color(150, 150, 0));
        put(START, new Color(255, 0, 0));
        put(END, new Color(255, 255, 255));
    }};

    int sleepMS = 0;
    int drawEveryXSteps = 10000;

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> maplines) throws IOException {

        // parse map
        lc.mapW = maplines.getFirst().length();
        lc.mapH = maplines.size();
        lc.map = new char[lc.mapH][lc.mapW];
        for (int y = 0; y < maplines.size(); y++) {
            var mapline = maplines.get(y);
            for (int x = 0; x < mapline.length(); x++) {
                lc.map[y][x] = mapline.charAt(x);
                if (lc.map[y][x] == START) {
                    lc.reindeerPos.y = y;
                    lc.reindeerPos.x = x;
                    lc.map[y][x] = FLOOR;
                } else if (lc.map[y][x] == END) {
                    lc.endPos.y = y;
                    lc.endPos.x = x;
                    lc.map[y][x] = FLOOR;
                } else if (lc.map[y][x] == '.') {
                    lc.map[y][x] = FLOOR;
                } else if (lc.map[y][x] == '#') {
                    lc.map[y][x] = WALL;
                }
            }
        }

//        openWindow("AOC 2024 Day 16", 50 + lc.mapW * tilesize.x * 2, 100 + lc.mapH * tilesize.y);
//        winframe.g.setFont(new Font("Consolas", Font.BOLD, 32));

        // Part 1
        var results = new Results();
        results.minScore = Integer.MAX_VALUE;
        results.currentScore = 0;
        results.iterations = 0;

        System.out.println("Initial Map 1:");
        dumpMap(lc.map, results, false);

        // TODO 1. Breitensuche statt Tiefensuche?
        // TODO 2. Breitensuche statt Tiefensuche?
//        calcLowestScore(lc.map, lc.reindeerPos, RIGHT, results);
        var result = findOptimalPath(lc.map, lc.reindeerPos.y, lc.reindeerPos.x, RIGHT, lc.endPos.y, lc.endPos.x);
        var paths = (List<List<int[]>>) result.get("paths");
        var pathCount = paths.size();
        for (List<int[]> path : paths) {
            drawPathToMap(lc.map, path);
        }
        results.minScore = (int) result.get("cost");

        System.out.println("Final Map 1:");
        dumpMap(lc.map, results, false);

        System.out.println("----------------------------------");
        System.out.printf("Part 1 lowest score: %d (%d Paths)\n", results.minScore, pathCount);
    }

    protected void calcLowestScore(char[][] map, Pos pos, int dir, Results results) {

        // 3 Richtungen testen
        int ndir;
        Pos npos;
        for (int ndirTmp = dir - 1; ndirTmp <= dir + 1; ndirTmp++) {
            ndir = Math.floorMod(ndirTmp, 4);
            npos = pos.plus(dirs[ndir]);
            if (map[npos.y][npos.x] == FLOOR) {
                int cost = ndir == dir ? 1 : 1001;
                if (results.currentScore + cost < results.minScore) {
                    map[pos.y][pos.x] = dirChars[ndir];
                    results.currentScore += cost;
                    results.iterations++;
                    if (winframe != null) {
                        if (drawEveryXSteps == 1) {
                            drawMapPos(map, winframe.g, results, pos, true);
                        } else if (drawEveryXSteps > 1 && results.iterations % drawEveryXSteps == 0) {
                            drawMap(map, winframe.g, results, false);
                        }
                    }
                    calcLowestScore(map, npos, ndir, results);
                    results.currentScore -= cost;
                    map[pos.y][pos.x] = FLOOR;
                    if (winframe != null) {
                        if (drawEveryXSteps == 1) {
                            drawMapPos(map, winframe.g, results, pos, true);
                        } else if (drawEveryXSteps > 1 && results.iterations % drawEveryXSteps == 0) {
                            drawMap(map, winframe.g, results, false);
                        }
                    }
                }

            } else if (map[npos.y][npos.x] == END) {
                map[pos.y][pos.x] = dirChars[ndir];
                int cost = ndir == dir ? 1 : 1001;
                results.currentScore += cost;
                results.iterations++;
                if (results.currentScore < results.minScore) {
                    results.minScore = results.currentScore;
                    dumpMap(map, results, true);
                }
                results.currentScore -= cost;
                map[pos.y][pos.x] = FLOOR;
            }
        }
    }

    public void drawPathToMap(char[][] map, List<int[]> path) {

        char chr = CUP;
        for (int i = 0; i < path.size(); i++) {
            var current = path.get(i);
            if (i < path.size() - 1) {
                var next = path.get(i + 1);
                if (next[0] > current[0]) {
                    chr = CDOWN;
                } else if (next[0] < current[0]) {
                    chr = CUP;
                } else if (next[1] > current[1]) {
                    chr = CRIGHT;
                } else if (next[1] < current[1]) {
                    chr = CLEFT;
                }
            } else {

            }
            map[current[0]][current[1]] = chr;
        }
    }

    protected static class QuEl {
        int y, x;
        int cost;
        int dir;
        int prevY, prevX;

        public QuEl(int y, int x, int cost, int dir, int prevY, int prevX) {
            this.y = y;
            this.x = x;
            this.cost = cost;
            this.dir = dir;
            this.prevY = prevY;
            this.prevX = prevX;
        }
    }

    // Methode zur Berechnung des optimalen Pfades
    public Map<String, Object> findOptimalPath(char[][] maze, int startY, int startX, int startDir, int endY, int endX) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Warteschlange für die Breitensuche mit {y, x, Kosten, Richtung, VorgängerY, VorgängerX}
        PriorityQueue<QuEl> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));
        queue.add(new QuEl(startY, startX, 0, startDir, -1, -1)); // Startpunkt mit Kosten 0 und vorgegebener Richtung

        // Kostenmatrix initialisieren
        int[][] cost = new int[rows][cols];
        for (int[] row : cost) Arrays.fill(row, Integer.MAX_VALUE);
        cost[startY][startX] = 0;

        /* TODO:
         *   Die cost-Matrix speichert den günstigsten Wert, um zu diesem Punkt zu kommen, egal, von wo man kommt. Das
         *   ist ein Problem, denn es kann sein, dass ein zweiter Weg an dieser Stelle zwar teurer ist, aber an Ende
         *   (d.h. im Ziel) genauso teuer wie der andere Weg.
         *   Lösung: die cost-Matrix muss die Richtung, aus der der Weg kommt, berücksichtigen. Nur, wenn ein Pfad aus
         *   der gleichen Richtung kommt und teurer ist, sollte er verworfen werden.
         *   Beim folgenden Maze ist der obere Pfad an der Stelle (y,x) = (3,4) günstiger als der untere. Allerdings
         *   sind die beim nächsten Schritt (y,x) = (3,5) wieer gleich teuer, denn der obere Pfad biegt ab (1000 Punkte),
         *   während der untere geradeaus geht.
         *   Die KIs kommen mit dieser Kostenstruktur scheinbar nicht zurecht. Sie finden zwar den grundsätzlich
         *   günstigsten Pfad, schaffen es aber nicht, alle möglichen günstigsten Pfade zu berechnen.
         */

        // Vorgänger-Matrix für mehrere Pfade
        Map<String, List<int[]>> predecessors = new HashMap<>();

        while (!queue.isEmpty()) {
            QuEl current = queue.poll();
            int y = current.y;
            int x = current.x;
            int currentCost = current.cost;
            int currentDir = current.dir;
            int prevY = current.prevY;
            int prevX = current.prevX;

            String key = y + "," + x;
            if (!predecessors.containsKey(key)) {
                predecessors.put(key, new ArrayList<>());
            }

            if (prevY != -1 && prevX != -1) {
                predecessors.get(key).add(new int[]{prevY, prevX});
            }

            // Wenn das Ziel erreicht ist, den Pfadpreis zurückgeben
            if (y == endY && x == endX) {
                int minimalCost = currentCost;
                List<List<int[]>> allPaths = new ArrayList<>();

                reconstructPaths(predecessors, endY, endX, new ArrayList<>(), allPaths);

                Map<String, Object> result = new HashMap<>();
                result.put("cost", minimalCost);
                result.put("paths", allPaths);
                return result;
            }

            // Über alle Bewegungsrichtungen iterieren
            for (int dir = 0; dir < dirs.length; dir++) {
                int newY = y + dirs[dir].y;
                int newX = x + dirs[dir].x;
                int newCost = currentCost + 1; // Standardkostenschritt

                // Kosten für Richtungswechsel hinzufügen, wenn nötig
                if (currentDir != -1 && currentDir != dir) {
                    newCost += 1000;
                }

                // Überprüfen, ob die neuen Koordinaten begehbar sind
                if (maze[newY][newX] == FLOOR) {
                    // Wenn der neue Pfad günstiger ist, aktualisieren und zur Warteschlange hinzufügen
                    if (newCost <= cost[newY][newX]) {
                        cost[newY][newX] = newCost;
                        queue.add(new QuEl(newY, newX, newCost, dir, y, x));
                    }
                }
            }
        }

        // Kein Weg gefunden
        return Collections.emptyMap();
    }

    private static void reconstructPaths(Map<String, List<int[]>> predecessors, int y, int x, List<int[]> currentPath, List<List<int[]>> allPaths) {
        currentPath.add(new int[]{y, x});

        String key = y + "," + x;
        if (!predecessors.containsKey(key) || predecessors.get(key).isEmpty()) {
            List<int[]> path = new ArrayList<>(currentPath);
            Collections.reverse(path);
            allPaths.add(path);
            return;
        }

        for (int[] pred : predecessors.get(key)) {
            reconstructPaths(predecessors, pred[0], pred[1], new ArrayList<>(currentPath), allPaths);
        }
    }

    private void dumpMap(char[][] map, Results results, boolean lowest) {

        System.out.printf("Map Part 1, Score: %d, Iteration: %d\n", results.minScore, results.iterations);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                printChar(map[y][x],
                        switch (map[y][x]) {
                            case WALL -> GREEN;
                            case FLOOR -> BLUE;
                            case START -> RED_BRIGHT;
                            case END -> WHITE_BRIGHT;
                            case CUP, CRIGHT, CDOWN, CLEFT -> YELLOW_BRIGHT2;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        },
                        switch (map[y][x]) {
                            case WALL -> BG_BLACK;
                            case FLOOR -> BG_BLACK;
                            case START -> BG_BLACK;
                            case END -> BG_BLACK;
                            case CUP, CRIGHT, CDOWN, CLEFT -> BG_BLACK;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        });
            }
            System.out.println();
        }

        if (winframe != null && winframe.g != null) {
            drawMap(map, winframe.g, results, lowest);
        }
    }

    private void drawMapPos(char[][] map, Graphics2D g, Results results, Pos pos, boolean wasPath) {
        if (g == null) {
            return;
        }

        g.setColor(Color.WHITE);
        g.fillRect(20, 0, 100, 60);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(results.iterations), 20, 55);

        int xoffset = 0;

        g.setColor(wasPath ? tileColors.get(WASPATH) : tileColors.get(map[pos.y][pos.x]));
        g.fillRect(20 + xoffset + pos.x * tilesize.x, 60 + pos.y * tilesize.y, tilesize.x, tilesize.y);

        if (sleepMS > 0) {
            try {
                Thread.sleep(sleepMS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void drawMap(char[][] map, Graphics2D g, Results results, boolean lowest) {
        if (g == null) {
            return;
        }

        g.setColor(Color.WHITE);
        g.fillRect(20, 0, 300, 60);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(results.iterations), 20, 55);

        int xoffset = 0;
        if (lowest) {
            xoffset = 20 + lc.mapW * tilesize.x;
        }

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                g.setColor(tileColors.get(map[y][x]));
                g.fillRect(20 + xoffset + x * tilesize.x, 60 + y * tilesize.y, tilesize.x, tilesize.y);
            }
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
