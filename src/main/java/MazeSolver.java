import java.util.*;

public class MazeSolver {

    // Bewegungsrichtungen: oben, rechts, unten, links
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    // Methode zur Berechnung der minimalen Kosten und des optimalen Pfades
    public static Map<String, Object> findOptimalPathWithCost(int[][] maze, int startY, int startX, int startDir, int endY, int endX) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Warteschlange für die Breitensuche mit {y, x, Kosten, Richtung}
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.add(new int[]{startY, startX, 0, startDir}); // Startpunkt mit Kosten 0 und vorgegebener Richtung

        // Kostenmatrix initialisieren
        int[][] cost = new int[rows][cols];
        for (int[] row : cost) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        cost[startY][startX] = 0;

        // Vorgänger-Matrix für die Rekonstruktion des Pfades
        int[][][] predecessors = new int[rows][cols][2];
        for (int[][] row : predecessors) {
            for (int[] cell : row) {
                Arrays.fill(cell, -1);
            }
        }

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
                if (newY >= 0 && newY < rows && newX >= 0 && newX < cols && maze[newY][newX] == 0 && newCost < cost[newY][newX]) {
                    cost[newY][newX] = newCost;
                    queue.add(new int[]{newY, newX, newCost, dir});
                    predecessors[newY][newX] = new int[]{y, x};
                }
            }
        }

        // Minimalen Pfadpreis finden und Pfad rekonstruieren
        if (cost[endY][endX] == Integer.MAX_VALUE) {
            return Collections.emptyMap(); // Kein Weg gefunden
        }

        List<int[]> path = new ArrayList<>();
        for (int y = endY, x = endX; y != -1 && x != -1; ) {
            path.add(new int[]{y, x});
            int[] prev = predecessors[y][x];
            y = prev[0];
            x = prev[1];
        }
        Collections.reverse(path);

        Map<String, Object> result = new HashMap<>();
        result.put("cost", cost[endY][endX]);
        result.put("path", path);
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

        Map<String, Object> result = findOptimalPathWithCost(maze, startY, startX, startDir, endY, endX);
        if (result.isEmpty()) {
            System.out.println("Kein Weg gefunden.");
        } else {
            System.out.println("Minimaler Pfadpreis: " + result.get("cost"));
            System.out.println("Optimaler Pfad:");
            List<int[]> path = (List<int[]>) result.get("path");
            for (int[] step : path) {
                System.out.print(Arrays.toString(step) + " -> ");
            }
            System.out.println("Ende");
        }
    }
}
