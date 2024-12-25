package com.thgross;

import com.thgross.aoc.Application;
import com.thgross.aoc.StringMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AOC2024_21 extends Application {

    String inputFilename = "input21-t1b.txt";

    public static void main(String[] args) {
        var app = (new AOC2024_21());
        app.run(app.inputFilename);
    }

    public class Pad {
        Map<Character, Pos> buttons = new HashMap<>();
        Map<Character, Map<Character, List<Character>>> paths = new HashMap<>();
        Character currentButton;
        Character[][] map;

        public void init() {
            reset();
            precalcPaths();
            mapFromButtons();
        }

        public void reset() {
            currentButton = 'A';
        }

        protected Character dirToChar(int dir) {
            return switch (dir) {
                case TOP -> '^';
                case RIGHT -> '>';
                case BOTTOM -> 'v';
                case LEFT -> '<';
                default -> '?';
            };
        }

        protected void mapFromButtons() {
            int w = 0, h = 0;
            for (Map.Entry<Character, Pos> entry : buttons.entrySet()) {
                Pos pos = entry.getValue();
                if (pos.y >= h) {
                    h = pos.y + 1;
                }
                if (pos.x >= w) {
                    w = pos.x + 1;
                }
            }
            map = new Character[h][w];
            for (Map.Entry<Character, Pos> entry : buttons.entrySet()) {
                Character chr = entry.getKey();
                Pos pos = entry.getValue();
                map[pos.y][pos.x] = chr;
            }
        }

        private void dumpMapRowBorder(int row) {
            for (int x = 0; x < map[row].length; x++) {
                System.out.print("+---");
            }
            System.out.print("+");
            System.out.println();
        }

        public void dump() {
            dumpMapRowBorder(0);
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == null) {
                        System.out.print("|   ");
                    } else {
                        System.out.printf("| %c ", map[y][x]);
                    }
                }
                System.out.print("|");
                System.out.println();
                dumpMapRowBorder(y);
            }
        }

        protected void precalcPaths() {
            // calculate paths
            // source char
            buttons.forEach((chr, pos) -> {
                var paths = new HashMap<Character, List<Character>>();
                // target char
                buttons.forEach((chr2, pos2) -> {
                    var path = new ArrayList<Character>();
                    // right
                    if (pos2.x > pos.x) {
                        for (int i = pos.x; i < pos2.x; i++) {
                            path.add(dirToChar(RIGHT));
                        }
                    }
                    // down
                    if (pos2.y > pos.y) {
                        for (int i = pos.y; i < pos2.y; i++) {
                            path.add(dirToChar(BOTTOM));
                        }
                    }
                    // up
                    if (pos2.y < pos.y) {
                        for (int i = pos2.y; i < pos.y; i++) {
                            path.add(dirToChar(TOP));
                        }
                    }
                    // left
                    if (pos2.x < pos.x) {
                        for (int i = pos2.x; i < pos.x; i++) {
                            path.add(dirToChar(LEFT));
                        }
                    }

                    paths.put(chr2, path);
                });

                this.paths.put(chr, paths);
            });
        }

        public List<Character> getPaths(List<Character> btns) {
            List<Character> btlist = new ArrayList<>();
            for (int i = 0; i < btns.size(); i++) {
                var nextButton = btns.get(i);
                btlist.addAll(paths.get(currentButton).get(nextButton));
                // Press 'A' to confirm Button
                btlist.add('A');
                currentButton = nextButton;
            }
            return btlist;
        }
    }

    public class Numpad extends Pad {
        public Numpad() {

            buttons.put('7', new Pos(0, 0));
            buttons.put('8', new Pos(0, 1));
            buttons.put('9', new Pos(0, 2));
            buttons.put('4', new Pos(1, 0));
            buttons.put('5', new Pos(1, 1));
            buttons.put('6', new Pos(1, 2));
            buttons.put('1', new Pos(2, 0));
            buttons.put('2', new Pos(2, 1));
            buttons.put('3', new Pos(2, 2));
            buttons.put('0', new Pos(3, 1));
            buttons.put('A', new Pos(3, 2));

            currentButton = 'A';

            init();
        }
    }

    public class Dirpad extends Pad {
        public Dirpad() {
            buttons.put('^', new Pos(0, 1));
            buttons.put('A', new Pos(0, 2));
            buttons.put('<', new Pos(1, 0));
            buttons.put('v', new Pos(1, 1));
            buttons.put('>', new Pos(1, 2));

            currentButton = 'A';

            init();
        }
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        List<List<Character>> codes = new ArrayList<>();
        for (String line : lines) {
            List<Character> code = new ArrayList<>();
            for (char c : line.toCharArray()) {
                code.add(c);

            }
            codes.add(code);
        }

        var smap = new StringMap(9, 70, ".");

        var nppad = new Numpad();
        var dpad1 = new Dirpad();
        var dpad2 = new Dirpad();

        System.out.println("----------------------------------");
        // Part 1
        Long part1Complexity = 0L;
        List<Character> dpPaths1;
        List<Character> dpPaths2;
        List<Character> myPath;
        nppad.reset();
        dpad1.reset();
        dpad2.reset();

        nppad.dump();
        dpad1.dump();

        for (List<Character> code : codes) {
            dpPaths1 = nppad.getPaths(code);
            dpPaths2 = dpad1.getPaths(dpPaths1);
            myPath = dpad2.getPaths(dpPaths2);

            var pathString = code.stream()
                    .map(e -> e.toString())
                    .collect(Collectors.joining());
            var codeVal = (long) Integer.parseInt(pathString.substring(0, 3));
            part1Complexity += codeVal * myPath.size();

            System.out.printf("%s: %s\n", pathToString(code), pathToString(myPath));
        }
        System.out.println("------------------------------------");
        System.out.printf("Part 1 Complexity: %d\n", part1Complexity);
    }

    private String pathToString(List<Character> path) {
        StringBuilder sb = new StringBuilder();
        for (Character c : path) {
            sb.append(c);
        }

        return sb.toString();
    }
}
