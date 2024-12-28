package com.thgross.aoc2024;

import java.util.*;

public class MazeSolverGemini {

    static class Node implements Comparable<Node> {
        int x, y, cost;
        int lastDirection;

        public Node(int x, int y, int cost, int lastDirection) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.lastDirection = lastDirection;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    public static Result solveMaze(char[][] maze, int startX, int startY, int endX, int endY) {
        int rows = maze.length;
        int cols = maze[0].length;

        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        int[][] prevX = new int[rows][cols];
        int[][] prevY = new int[rows][cols];

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(startX, startY, 0, -1));
        dist[startX][startY] = 0;

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int x = current.x;
            int y = current.y;
            int cost = current.cost;
            int lastDirection = current.lastDirection;

            if (x == endX && y == endY) {
                List<int[]> path = reconstructPath(prevX, prevY, startX, startY, endX, endY);
                return new Result(path, cost);
            }

            if (cost > dist[x][y]) continue; // WICHTIG: Überprüfe, ob der aktuelle Pfad bereits teurer ist

            for (int i = 0; i < 4; i++) {
                int newX = x + directions[i][0];
                int newY = y + directions[i][1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && maze[newX][newY] != '#') {
                    int newCost = cost + 1;
                    if (lastDirection != -1 && lastDirection != i) {
                        newCost += 1000;
                    }

                    if (newCost < dist[newX][newY]) {
                        dist[newX][newY] = newCost;
                        prevX[newX][newY] = x;
                        prevY[newX][newY] = y;
                        pq.offer(new Node(newX, newY, newCost, i));
                    }
                }
            }
        }

        return new Result(null, -1);
    }

    private static List<int[]> reconstructPath(int[][] prevX, int[][] prevY, int startX, int startY, int endX, int endY) {
        List<int[]> path = new ArrayList<>();
        int x = endX;
        int y = endY;

        while (x != startX || y != startY) {
            path.add(0, new int[]{x, y});
            int tempX = x;
            x = prevX[x][y];
            y = prevY[tempX][y];
        }
        path.add(0, new int[]{startX, startY});
        return path;
    }

    public static class Result {
        public List<int[]> path;
        public int cost;

        public Result(List<int[]> path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }

    private static void printResult(Result result, char[][] maze){
        System.out.println("Maze:");
        for(char[] row: maze)
            System.out.println(Arrays.toString(row));
        if (result.path != null) {
            System.out.println("Pfad gefunden:");
            for (int[] point : result.path) {
                System.out.println(Arrays.toString(point));
            }
            System.out.println("Gesamtkosten: " + result.cost);
        } else {
            System.out.println("Kein Pfad gefunden.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        char[][] maze = {
                {'S', '.', '.', '#', '.'},
                {'.', '#', '.', '.', '.'},
                {'.', '.', '.', '#', '.'},
                {'#', '.', '#', '.', 'E'}
        };
        Result result = solveMaze(maze, 0, 0, 3, 4);
        printResult(result, maze);

        char[][] maze2 = {
                {'S', '.', '.', '.', '.'},
                {'.', '.', '#', '.', '.'},
                {'.', '.', '#', '.', '.'},
                {'.', '.', '#', '.', 'E'}
        };
        Result result2 = solveMaze(maze2, 0, 0, 3, 4);
        printResult(result2, maze2);

        char[][] maze3 = {
                {'S', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'#', '#', '#', '#', '.'},
                {'.', '.', '.', '.', 'E'}
        };
        Result result3 = solveMaze(maze3, 0, 0, 3, 4);
        printResult(result3, maze3);

        char[][] maze4 = {
                {'S', '.', '.', '.', '.'},
                {'.', '#', '#', '#', '.'},
                {'.', '.', '.', '.', '.'},
                {'#', '#', '#', '.', 'E'}
        };
        Result result4 = solveMaze(maze4, 0, 0, 3, 4);
        printResult(result4, maze4);

        char[][] maze5 = {
                {'S', '.', '.', '#', '.'},
                {'.', '.', '.', '#', '.'},
                {'#', '.', '.', '#', '.'},
                {'.', '.', '.', '#', 'E'}
        };
        Result result5 = solveMaze(maze5, 0, 0, 3, 4);
        printResult(result5, maze5);

        char[][] maze6 = {
                {'S', '.', '#', '#', '.'},
                {'.', '.', '#', '.', '.'},
                {'.', '.', '#', '.', '.'},
                {'#', '.', '#', '.', 'E'}
        };
        Result result6 = solveMaze(maze6, 0, 0, 3, 4);
        printResult(result6, maze6);

        char[][] maze7 = {
                {'S', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '#', '#', '.', '.'},
                {'.', '.', '.', '.', 'E'}
        };
        Result result7 = solveMaze(maze7, 0, 0, 3, 4);
        printResult(result7, maze7);

        char[][] maze8 = {
                {'S', '.', '.', '#', 'E'},
                {'.', '#', '.', '.', '.'},
                {'.', '#', '#', '#', '.'},
                {'.', '.', '.', '.', '.'}
        };
        Result result8 = solveMaze(maze8, 0, 0, 0, 4);
        printResult(result8, maze8);

    }
}