package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AOC2024_20 extends Application {

    public static final char FLOOR = '·';
    public static final char WALL = '#';
    public static final char PATH = 'O';
    public static final char START = 'S';
    public static final char END = 'E';

//    String inputFilename = "input20-t1.txt"; int minStepsSaved = 0;
    String inputFilename = "input20.txt"; int minStepsSaved = 100;

    public static void main(String[] args) {
        var app = (new AOC2024_20());
        app.run(app.inputFilename);
    }

    int mapW;
    int mapH;

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        mapH = lines.size();
        mapW = lines.getFirst().length();

        Position start = new Position(0, 0),
                end = new Position(0, 0);

        char[][] map = new char[mapH][mapW];
        for (int i = 0; i < lines.size(); i++) {
            var lchars = lines.get(i).toCharArray();
            for (int i1 = 0; i1 < lchars.length; i1++) {
                if (lchars[i1] == START) {
                    map[i][i1] = FLOOR;
                    start = new Position(i, i1);
                } else if (lchars[i1] == END) {
                    map[i][i1] = FLOOR;
                    end = new Position(i, i1);
                } else {
                    map[i][i1] = lchars[i1];
                }
            }
        }

        System.out.println("----------------------------------");
        // Part 1
        int steps1 = 0;
        long cheatCount1 = 0;
        long variantsTested1 = 0;
        long variantsFaster1 = 0;
        resetMap(map);
        var path1 = findShortestPath(map, start, end);
        steps1 = path1.size() - 1;
        // find cheats by removing single Walls
        for (int row = 1; row < map.length - 1; row++) {
            for (int col = 1; col < map[row].length - 1; col++) {
                if (map[row][col] == WALL) {
                    // only try WALLs with at least 2 connected floors
                    int fcount = 0;
                    if (map[row - 1][col] == FLOOR) fcount++;
                    if (map[row][col + 1] == FLOOR) fcount++;
                    if (map[row + 1][col] == FLOOR) fcount++;
                    if (map[row][col - 1] == FLOOR) fcount++;
                    if(fcount >= 2) {
                        // try removing this wall
                        map[row][col] = FLOOR;
                        path1 = findShortestPath(map, start, end);
                        variantsTested1++;
                        if(!path1.isEmpty()) {
                            int pSteps = path1.size() - 1;
                            if(pSteps < steps1 && steps1 - pSteps >= minStepsSaved) {
                                cheatCount1++;
                            }
                        }
                        // Put wall back in
                        map[row][col] = WALL;
                    }
                }
            }
        }
        drawPathToMap(map, path1);
        dumpMap(map);
        System.out.printf("Part 1 steps: %d\n", steps1);
        System.out.printf("Part 1 cheatcount: %d (at least %d picoseconds saved; %d variants tested)\n", cheatCount1, minStepsSaved, variantsTested1);
    }

    private void drawPathToMap(char[][] map, List<Position> path) {
        for (Position position : path) {
            map[position.row][position.col] = PATH;
        }
    }

    private void dumpMap(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                printChar(map[y][x],
                        switch (map[y][x]) {
                            case WALL -> GREEN;
                            case FLOOR -> BLUE;
                            case PATH -> YELLOW_BRIGHT2;
                            default -> throw new RuntimeException("unknown map tile " + map[y][x]);
                        });
            }
            System.out.println();
        }
    }

    private void resetMap(char[][] map) {
        for (char[] chars : map) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != WALL) {
                    chars[i] = FLOOR;
                }
            }
        }
    }

    // Klasse zur Darstellung einer Position im Labyrinth
    public static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "[" + row + ", " + col + "]";
        }
    }

    // Methode, um den kürzesten Pfad in einem Labyrinth zu finden
    public static List<Position> findShortestPath(char[][] maze, Position start, Position end) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Bewegungen: oben, unten, links, rechts
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Warteschlange für die BFS
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);

        // Matrix zur Verfolgung der besuchten Zellen
        boolean[][] visited = new boolean[rows][cols];
        visited[start.row][start.col] = true;

        // Karte zur Rückverfolgung des Pfads
        Map<Position, Position> parentMap = new HashMap<>();
        parentMap.put(start, null);

        // BFS-Schleife
        while (!queue.isEmpty()) {
            Position current = queue.poll();

            // Ziel erreicht?
            if (current.equals(end)) {
                return reconstructPath(parentMap, end);
            }

            // Nachbarn durchsuchen
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                // Prüfen, ob der Nachbar innerhalb des Labyrinths und begehbar ist
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && maze[newRow][newCol] == FLOOR && !visited[newRow][newCol]) {

                    visited[newRow][newCol] = true;
                    Position neighbor = new Position(newRow, newCol);
                    queue.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        // Kein Weg gefunden
        return Collections.emptyList();
    }

    // Methode zur Rückverfolgung des Pfads
    private static List<Position> reconstructPath(Map<Position, Position> parentMap, Position end) {
        List<Position> path = new ArrayList<>();
        Position current = end;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

}
