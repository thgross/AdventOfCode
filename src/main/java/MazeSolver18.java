import java.util.*;

public class MazeSolver18 {

    // Klasse zur Darstellung einer Position im Labyrinth
    static class Position {
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
    public static List<Position> findShortestPath(int[][] maze, Position start, Position end) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Bewegungen: oben, unten, links, rechts
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

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
                        && maze[newRow][newCol] == 0 && !visited[newRow][newCol]) {

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

    // Hauptmethode zur Demonstration
    public static void main(String[] args) {
        int[][] maze = {
                { 0, 1, 0, 0, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0 },
                { 0, 1, 1, 1, 0 },
                { 0, 0, 0, 0, 0 }
        };

        Position start = new Position(0, 0); // Startposition
        Position end = new Position(4, 4);   // Zielposition

        List<Position> path = findShortestPath(maze, start, end);

        if (path.isEmpty()) {
            System.out.println("Kein Weg gefunden.");
        } else {
            System.out.println("Kürzester Weg:");
            for (Position step : path) {
                System.out.println(step);
            }
        }
    }
}
