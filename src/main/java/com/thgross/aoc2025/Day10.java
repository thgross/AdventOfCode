package com.thgross.aoc2025;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Day10 extends Application {

    String inputFilename = "aoc2025/input10.txt";

    static void main() {
        var app = (new Day10());
        app.run(app.inputFilename);
    }

    class Machine {
        short target = 0;
        short[] buttons;
        List<Short> joltage = new ArrayList<>();

        public String toString() {
            var sb = new StringBuilder();
            sb.append("[");
            sb.append(getBitsReversed(target));
            sb.append("]");
            sb.append(" ");
            for (int i = 0; i < buttons.length; i++) {
                sb.append("(");
                sb.append(getBitsReversed(buttons[i]));
                sb.append(")");
            }
            sb.append(" ");
            sb.append("{");
            for (Short j : joltage) {
                sb.append(j);
                sb.append(",");
            }
            sb.append("}");
            sb.append(" ");
            sb.append("\n");
            return sb.toString();
        }
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        int part1Presses = 0;

        var machines = new ArrayList<Machine>();

        for (String line : lines) {
            var m = new Machine();
            var tmpButtons = new ArrayList<Short>();
            var parts = line.split(" ");
            for (String part : parts) {
                if (part.startsWith("[")) {
                    // Target Lights
                    for (int c = 1; c < part.length() - 1; c++) {
                        if (part.charAt(c) == '#') {
                            m.target |= (short) (1 << c - 1);
                        }
                    }

                } else if (part.startsWith("(")) {
                    // Buttons
                    short btn = 0;
                    for (int c = 1; c < part.length() - 1; c += 2) {
                        short offset = Short.parseShort(String.valueOf(part.charAt(c)));
                        btn |= (short) (1 << offset);
                    }
                    tmpButtons.add(btn);

                } else if (part.startsWith("{")) {
                    // joltage Requirements
                    var partsJ = part.substring(1, part.length() - 1).split(",");
                    for (String s : partsJ) {
                        m.joltage.add((short) Integer.parseInt(s));
                    }
                }
                m.buttons = new short[tmpButtons.size()];
                for (int i = 0; i < tmpButtons.size(); i++) {
                    m.buttons[i] = tmpButtons.get(i);
                }
            }
            machines.add(m);
        }

        for (Machine machine : machines) {
            int presses = bfs(machine.target, machine.buttons);
            System.out.printf("%s => %d presses%n", machine, presses);
            if (presses >= 0) {
                part1Presses += presses;
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Presses: %d\n", part1Presses);
    }

    public int bfs(short target, short[] buttons) {
        if (target == 0) return 0;

        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(new int[]{0, 0});
        visited.add(0);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            int state = current[0];
            int depth = current[1];

            for (short button : buttons) {
                int newState = state ^ button;
                if (newState == target) {
                    return depth + 1;
                }
                if (!visited.contains(newState)) {
                    visited.add(newState);
                    queue.add(new int[]{newState, depth + 1});
                }
            }
        }

        return -1;
    }

    public static String getBitsReversed(short wert) {
        StringBuilder sb = new StringBuilder();

        // Wir starten beim niedrigstwertigen Bit (0) und gehen hoch bis 15
        for (int i = 0; i <= 15; i++) {
            int bit = (wert >> i) & 1;

            // Bit anhängen
            if (bit == 1) {
                sb.append("#");
            } else {
                sb.append(".");
            }
        }

        return sb.toString();
    }
}
