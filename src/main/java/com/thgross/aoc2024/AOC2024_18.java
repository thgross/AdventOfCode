package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AOC2024_18 extends Application {

    public static final char FLOOR = '·';
    public static final char BYTE = '#';
    public static final char PATH = 'O';

    String inputFilename = "aoc2024/input18.txt"; int bytecount = 1024; int mapWH = 71; Position startpos = new Position(70, 70);
//    String inputFilename = "input18-t1.txt"; int bytecount = 12; int mapWH = 7; Position startpos = new Position(6, 6);

    public static void main(String[] args) {
        var app = (new AOC2024_18());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {


        char[][] map = new char[mapWH][mapWH];
        List<Position> bytes = new ArrayList<>();

        Pattern patternPos = Pattern.compile("^(\\d+),(\\d+)$");

        for (String line : lines) {
            Matcher matcher = patternPos.matcher(line);
            if (matcher.find()) {
                bytes.add(new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1))));
            }
        }

        System.out.println("----------------------------------");

        // Part 1
        int steps1 = 0;
        resetMap(map);
        bytesToMap(bytes, map, bytecount);
        var path1 = findShortestPath(map, new Position(0, 0), startpos);
        steps1 = path1.size() - 1;
        drawPathToMap(map, path1);
        dumpMap(map);
        System.out.printf("Part 1 steps: %s\n", steps1);

        // Part 2
        int steps2 = 0;
        resetMap(map);
        bytesToMap(bytes, map, bytecount);
        Position bytepos = new Position(0, 0);
        for (int bytenum = bytecount; bytenum < bytes.size(); bytenum++) {
            bytepos = bytes.get(bytenum);
            map[bytepos.row][bytepos.col] = BYTE;
            var path2 = findShortestPath(map, new Position(0, 0), startpos);
            if (path2.isEmpty()) {
                break;
            }
        }
        System.out.printf("Part 2 Bytepos: %d,%d\n", bytepos.col, bytepos.row);
    }

    private void bytesToMap(List<Position> bytes, char[][] map, int bytecount) {
        for (int i = 0; i < bytecount; i++) {
            map[bytes.get(i).row][bytes.get(i).col] = BYTE;
        }
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
                            case BYTE -> GREEN;
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
            Arrays.fill(chars, FLOOR);
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
