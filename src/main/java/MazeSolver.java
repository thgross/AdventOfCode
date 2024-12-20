import java.util.*;

public class MazeSolver {

    // Bewegungsrichtungen: oben, rechts, unten, links
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    // Methode zur Berechnung der minimalen Kosten und aller optimalen Pfade
    public static Map<String, Object> findOptimalPathsWithCost(int[][] maze, int startX, int startY, int startDir, int endX, int endY) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Warteschlange für die Breitensuche mit {x, y, Kosten, Richtung, VorgängerX, VorgängerY}
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.add(new int[]{startX, startY, 0, startDir, -1, -1}); // Startpunkt mit Kosten 0 und vorgegebener Richtung

        // Kostenmatrix initialisieren
        int[][] cost = new int[rows][cols];
        for (int[] row : cost) Arrays.fill(row, Integer.MAX_VALUE);
        cost[startX][startY] = 0;

        // Vorgänger-Matrix für mehrere Pfade
        Map<String, List<int[]>> predecessors = new HashMap<>();

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int currentCost = current[2];
            int currentDir = current[3];
            int prevX = current[4];
            int prevY = current[5];

            String key = x + "," + y;
            if (!predecessors.containsKey(key)) {
                predecessors.put(key, new ArrayList<>());
            }

            if (prevX != -1 && prevY != -1) {
                if (currentCost == cost[x][y]) {
                    predecessors.get(key).add(new int[]{prevX, prevY});
                }
            }

            // Wenn das Ziel erreicht ist
            if (x == endX && y == endY) {
                int minimalCost = currentCost;
                List<List<int[]>> allPaths = new ArrayList<>();

                reconstructPaths(predecessors, endX, endY, new ArrayList<>(), allPaths);

                Map<String, Object> result = new HashMap<>();
                result.put("cost", minimalCost);
                result.put("paths", allPaths);
                return result;
            }

            // Über alle Bewegungsrichtungen iterieren
            for (int dir = 0; dir < DIRECTIONS.length; dir++) {
                int newX = x + DIRECTIONS[dir][0];
                int newY = y + DIRECTIONS[dir][1];
                int newCost = currentCost + 1; // Standardkostenschritt

                // Kosten für Richtungswechsel hinzufügen, wenn nötig
                if (currentDir != -1 && currentDir != dir) {
                    newCost += 1000;
                }

                // Überprüfen, ob die neuen Koordinaten im Labyrinth sind und begehbar sind
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && maze[newX][newY] == 0) {
                    if (newCost < cost[newX][newY]) {
                        cost[newX][newY] = newCost;
                        queue.add(new int[]{newX, newY, newCost, dir, x, y});
                        predecessors.put(newX + "," + newY, new ArrayList<>(List.of(new int[]{x, y}))); // Initialisiert neue Vorgänger
                    } else if (newCost == cost[newX][newY]) {
                        predecessors.get(newX + "," + newY).add(new int[]{x, y}); // Fügt zusätzlichen Vorgänger hinzu
                    }
                }
            }
        }

        // Kein Weg gefunden
        return Collections.emptyMap();
    }

    private static void reconstructPaths(Map<String, List<int[]>> predecessors, int x, int y, List<int[]> currentPath, List<List<int[]>> allPaths) {
        currentPath.add(new int[]{x, y});

        String key = x + "," + y;
        if (!predecessors.containsKey(key) || predecessors.get(key).isEmpty()) {
            List<int[]> path = new ArrayList<>(currentPath);
            Collections.reverse(path);
            allPaths.add(path);
            return;
        }

        for (int[] pred : predecessors.get(key)) {
            List<int[]> newPath = new ArrayList<>(currentPath);
            reconstructPaths(predecessors, pred[0], pred[1], newPath, allPaths);
        }
    }

    public static void main(String[] args) {
        // Beispielirrgarten (0 = begehbar, 1 = Wand)
        int[][] maze = {
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        int startX = 0, startY = 0;
        int startDir = 1; // Beispiel: Startet in Richtung "rechts"
        int endX = 4, endY = 4;

        Map<String, Object> result = findOptimalPathsWithCost(maze, startX, startY, startDir, endX, endY);
        if (result.isEmpty()) {
            System.out.println("Kein Weg gefunden.");
        } else {
            System.out.println("Minimaler Pfadpreis: " + result.get("cost"));
            System.out.println("Alle minimalen Pfade:");
            List<List<int[]>> paths = (List<List<int[]>>) result.get("paths");
            int pnr = 0;
            for (List<int[]> path : paths) {
                System.out.printf("pnr: %4d: ", pnr++);
                for (int[] step : path) {
                    System.out.print(Arrays.toString(step) + " -> ");
                }
                System.out.println("Ende");
            }
        }
    }
}
