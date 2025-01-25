package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Application {

    String inputFilename = "aoc2015/input07.txt";

    public class Wire {
        String key;
        Integer signal;
        List<Gate> gates = new ArrayList<>();

        public Wire(String key) {
            this.key = key;
            if (key.matches("\\d+")) {
                setSignal(Integer.parseInt(key));
            } else {
                setSignal(null);
            }
        }

        public void setSignal(Integer signal) {
            this.signal = signal;
            if (this.signal != null) {
                for (Gate gate : gates) {
                    gate.newSignal();
                }
            }
        }
    }


    public class Gate {
        Wire input1;
        Wire input2;
        Wire output;
        String command;

        public Gate(String command, String input1, String input2, String output) {
            this.command = command;
        }

        public void newSignal() {
            // input incomming
            switch (command) {
                case "SET":
                    if (input1 != null && input1.signal != null) {
                        output.setSignal(input1.signal);
                    }
                    break;
                case "NOT":
                    if (input1 != null && input1.signal != null) {
                        output.setSignal((~input1.signal) & 0xFFFF);
                    }
                    break;
                case "AND":
                    if (input1 != null && input1.signal != null && input2 != null && input2.signal != null) {
                        output.setSignal((input1.signal & input2.signal) & 0xFFFF);
                    }
                    break;
                case "OR":
                    if (input1 != null && input1.signal != null && input2 != null && input2.signal != null) {
                        output.setSignal((input1.signal | input2.signal) & 0xFFFF);
                    }
                    break;
                case "LSHIFT":
                    if (input1 != null && input1.signal != null && input2 != null && input2.signal != null) {
                        output.setSignal((input1.signal << input2.signal) & 0xFFFF);
                    }
                    break;
                case "RSHIFT":
                    if (input1 != null && input1.signal != null && input2 != null && input2.signal != null) {
                        output.setSignal((input1.signal >> input2.signal) & 0xFFFF);
                    }
                    break;
            }
        }
    }

    public class Circuit {
        Map<String, Wire> wires = new HashMap<>();
        List<Gate> gates = new ArrayList<>();

        private Wire addWire(String key) {
            if (null != key) {
                if (!wires.containsKey(key)) {
                    wires.put(key, new Wire(key));
                }
                return wires.get(key);
            }

            return null;
        }

        public void addGate(String command, String input1, String input2, String output) {
            var w1 = addWire(input1);
            var w2 = addWire(input2);
            var wo = addWire(output);
            var g = new Gate(command, input1, input2, output);
            gates.add(g);

            if (null != w1) {
                w1.gates.add(g);
            }
            if (null != w2) {
                w2.gates.add(g);
            }
            if (null != wo) {
                wo.gates.add(g);
            }
        }
    }

    public static void main(String[] args) {
        var app = (new Day07());
        app.run(app.inputFilename);
    }

    Circuit circuit = new Circuit();

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var pSet = Pattern.compile("^(\\w+) -> ([a-z]+)$");   // lx -> er
        var pNOT = Pattern.compile("^NOT (\\w+) -> ([a-z]+)$");   // NOT lx -> er
        var pANDOR = Pattern.compile("^(\\w+) (AND|OR) (\\w+) -> ([a-z]+)$");  // as AND dw -> er
        var pSHIFT = Pattern.compile("^(\\w+) (LSHIFT|RSHIFT) (\\d+) -> ([a-z]+)$");  // as AND dw -> er
        for (String line : lines) {
            Matcher m;

            m = pSet.matcher(line);
            if (m.find()) {
                circuit.addGate("SET", m.group(1), null, m.group(2));
                continue;
            }

            m = pNOT.matcher(line);
            if (m.find()) {
                circuit.addGate("NOT", m.group(1), null, m.group(2));
                continue;
            }

            m = pANDOR.matcher(line);
            if (m.find()) {
                circuit.addGate(m.group(2), m.group(1), m.group(3), m.group(4));
                continue;
            }

            m = pSHIFT.matcher(line);
            if (m.find()) {
                circuit.addGate(m.group(2), m.group(1), m.group(3), m.group(4));
                continue;
            }

            throw new RuntimeException("Zeile konnte nicht geparst werden: " + line);
        }

        System.out.println(circuit);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Value on a: %d\n", circuit.wires.get("a").signal);
    }
}
