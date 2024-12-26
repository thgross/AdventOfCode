package com.thgross.day21;

import com.thgross.aoc.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thgross.aoc.Application.*;

public class Pad {
    protected Map<Character, Pos> buttons = new HashMap<>();
    protected Map<Character, Map<Character, List<Character>>> paths = new HashMap<>();
    protected Character currentButton;
    protected Pos currentPos;
    boolean buttonPressed = false;
    Character[][] map;
    Pad connectedPad;

    protected final int TOP = 0;
    protected final int RIGHT = 1;
    protected final int BOTTOM = 2;
    protected final int LEFT = 3;

    public void init() {
        reset();
        precalcPaths();
        mapFromButtons();
        connectedPad = null;
    }

    public void reset() {
        currentButton = 'A';
        currentPos = buttons.get(currentButton);
    }

    public void press(Character chr) {
        currentButton = chr;
        press();
    }

    public void press() {
        buttonPressed = true;

        if (connectedPad != null) {
            if (currentButton == 'A') {
                connectedPad.press();
            } else if (currentButton == '^') {
                connectedPad.move(TOP);
            } else if (currentButton == '>') {
                connectedPad.move(RIGHT);
            } else if (currentButton == 'v') {
                connectedPad.move(BOTTOM);
            } else if (currentButton == '<') {
                connectedPad.move(LEFT);
            }
        }
    }

    public void move(int dir) {
        release();
        currentPos.move(dir);
        currentButton = map[currentPos.y][currentPos.x];
    }

    public void connect(Pad connectedPad) {
        this.connectedPad = connectedPad;
    }

    public void release() {
        buttonPressed = false;
        if (connectedPad != null) {
            connectedPad.release();
        }
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

    public void dump() {
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
        }
    }

    public void dumpToStringMap(String[][] smap, int row, int col) {
        String colString;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] != null) {
                    colString = "";
                    if (currentButton == map[y][x]) {
                        colString = BLACK + BG_CYAN;
                        if (buttonPressed) {
                            colString = RED + BG_GREEN;
                        }
                    }
                    smap[row + 1 + y][col + 1 + x * 2] = colString + map[y][x].toString() + RESET;
                }
            }
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

    public void setCurrentButton(Character newButton) {
        currentButton = newButton;
    }
}
