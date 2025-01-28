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
        List<Gate> outputGates = new ArrayList<>();

        public Wire(String key) {
            this.key = key;
            if (key.matches("\\d+")) {
                signal = Integer.parseInt(key);
            } else {
                signal = null;
            }
        }

        public void setSignal(Integer signal) {
            this.signal = signal;
            if (this.signal != null) {
                for (Gate outputGate : outputGates) {
                    outputGate.calc();
                }
            }
        }

        @Override
        public String toString() {
            var s = new StringBuilder();
            s.append(signal);
            return s.toString();
        }
    }


    public class Gate {
        Wire input1;
        Wire input2;
        Wire output;
        String command;
        String commandRaw;

        public Gate(String command, Wire input1, Wire input2, Wire output, String commandRaw) {
            this.command = command;
            this.input1 = input1;
            this.input2 = input2;
            this.output = output;
            this.commandRaw = commandRaw;
        }

        public void calc() {
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

        public void addGate(String command, String input1, String input2, String output, String commandRaw) {
            var w1 = addWire(input1);
            var w2 = addWire(input2);
            var wo = addWire(output);
            var g = new Gate(command, w1, w2, wo, commandRaw);
            gates.add(g);


            if (null != w1) {
                w1.outputGates.add(g);
            }
            if (null != w2) {
                w2.outputGates.add(g);
            }

            g.calc();
        }

        @Override
        public String toString() {
            var s = new StringBuilder();

            s.append("wires: ");
            s.append(wires.toString());

            return s.toString();
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

        int linenr = -1;

        for (String line : lines) {
            Matcher m;
            linenr++;

            m = pSet.matcher(line);
            if (m.find()) {
                circuit.addGate("SET", m.group(1), null, m.group(2), line);
                continue;
            }

            m = pNOT.matcher(line);
            if (m.find()) {
                circuit.addGate("NOT", m.group(1), null, m.group(2), line);
                continue;
            }

            m = pANDOR.matcher(line);
            if (m.find()) {
                circuit.addGate(m.group(2), m.group(1), m.group(3), m.group(4), line);
                continue;
            }

            m = pSHIFT.matcher(line);
            if (m.find()) {
                circuit.addGate(m.group(2), m.group(1), m.group(3), m.group(4), line);
                continue;
            }

            throw new RuntimeException("Zeile konnte nicht geparst werden: " + line);
        }

        System.out.println(circuit);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Value on a: %d\n", circuit.wires.get("a").signal);
    }
}
