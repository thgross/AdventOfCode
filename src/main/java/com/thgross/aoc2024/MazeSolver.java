package com.thgross.aoc2024;

import java.util.*;

public class MazeSolver {

    // Bewegungsrichtungen: oben, rechts, unten, links
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    // Methode zur Berechnung der minimalen Kosten und aller optimalen Pfade
    public static Map<String, Object> findAllOptimalPathsWithCost(int[][] maze, int startY, int startX, int startDir, int endY, int endX) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Warteschlange für die Breitensuche mit {y, x, Kosten, Richtung}
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.add(new int[]{startY, startX, 0, startDir}); // Startpunkt mit Kosten 0 und vorgegebener Richtung

        // Kostenmatrix für jede Richtung initialisieren
        int[][][] cost = new int[rows][cols][DIRECTIONS.length];
        for (int[][] layer : cost) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }
        cost[startY][startX][startDir] = 0;

        // Map für alle Pfade mit minimalen Kosten
        Map<String, List<List<int[]>>> allPaths = new HashMap<>();
        String startKey = startY + "," + startX + "," + startDir;
        allPaths.put(startKey, new ArrayList<>());
        allPaths.get(startKey).add(new ArrayList<>(List.of(new int[]{startY, startX})));

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int y = current[0];
            int x = current[1];
            int currentCost = current[2];
            int currentDir = current[3];

            // Über alle Bewegungsrichtungen iterieren
            for (int dir = 0; dir < DIRECTIONS.length; dir++) {
                int newY = y + DIRECTIONS[dir][0];
                int newX = x + DIRECTIONS[dir][1];
                int newCost = currentCost + 1; // Standardkostenschritt

                // Kosten für Richtungswechsel hinzufügen, wenn nötig
                if (currentDir != -1 && currentDir != dir) {
                    newCost += 1000;
                }

                // Überprüfen, ob die neuen Koordinaten im Labyrinth sind und begehbar sind
                if (newY >= 0 && newY < rows && newX >= 0 && newX < cols && maze[newY][newX] == 0) {
                    if (newCost < cost[newY][newX][dir]) {
                        cost[newY][newX][dir] = newCost;
                        queue.add(new int[]{newY, newX, newCost, dir});

                        // Pfade aktualisieren
                        String key = newY + "," + newX + "," + dir;
                        allPaths.put(key, new ArrayList<>());
                        String parentKey = y + "," + x + "," + currentDir;
                        for (List<int[]> path : allPaths.get(parentKey)) {
                            List<int[]> newPath = new ArrayList<>(path);
                            newPath.add(new int[]{newY, newX});
                            allPaths.get(key).add(newPath);
                        }
                    } else if (newCost == cost[newY][newX][dir]) {
                        // Wenn gleiche Kosten, Pfade hinzufügen
                        String key = newY + "," + newX + "," + dir;
                        String parentKey = y + "," + x + "," + currentDir;
                        for (List<int[]> path : allPaths.get(parentKey)) {
                            List<int[]> newPath = new ArrayList<>(path);
                            newPath.add(new int[]{newY, newX});
                            allPaths.get(key).add(newPath);
                        }
                    }
                }
            }
        }

        // Minimalen Pfadpreis finden und alle Pfade rekonstruieren
        int minCost = Integer.MAX_VALUE;
        String endKey = null;
        List<List<int[]>> resultPaths = new ArrayList<>();
        for (int dir = 0; dir < DIRECTIONS.length; dir++) {
            if (cost[endY][endX][dir] < minCost) {
                minCost = cost[endY][endX][dir];
                endKey = endY + "," + endX + "," + dir;
                resultPaths = allPaths.getOrDefault(endKey, new ArrayList<>());
            } else if (cost[endY][endX][dir] == minCost) {
                endKey = endY + "," + endX + "," + dir;
                resultPaths.addAll(allPaths.getOrDefault(endKey, new ArrayList<>()));
            }
        }

        if (minCost == Integer.MAX_VALUE) {
            return Collections.emptyMap(); // Kein Weg gefunden
        }

        Map<String, Object> result = new HashMap<>();
        result.put("cost", minCost);
        result.put("paths", resultPaths);
        return result;
    }

    public static void main(String[] args) {
        // Beispielirrgarten (0 = begehbar, 1 = Wand)
//        int[][] maze = {
//                {0, 0, 0, 1, 0},
//                {1, 1, 0, 1, 0},
//                {0, 0, 0, 0, 0},
//                {0, 1, 1, 1, 0},
//                {0, 0, 0, 0, 0}
//        };
//
//        int startY = 0, startX = 0;
//        int startDir = 1; // Beispiel: Startet in Richtung "rechts"
//        int endY = 4, endX = 4;

        int[][] maze = {
                {0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0}
        };
        int startX = 0, startY = 0;
        int startDir = 1; // Beispiel: Startet in Richtung "rechts"
        int endY = 2, endX = 4;

        Map<String, Object> result = findAllOptimalPathsWithCost(maze, startY, startX, startDir, endY, endX);
        if (result.isEmpty()) {
            System.out.println("Kein Weg gefunden.");
        } else {
            System.out.println("Minimaler Pfadpreis: " + result.get("cost"));
            System.out.println("Alle optimalen Pfade:");
            List<List<int[]>> paths = (List<List<int[]>>) result.get("paths");
            for (List<int[]> path : paths) {
                for (int[] step : path) {
                    System.out.print(Arrays.toString(step) + " -> ");
                }
                System.out.println("Ende");
            }
        }
    }
}
