package com.thgross.aoc2024;

import com.thgross.aoc.Application;
import com.thgross.aoc2024.day24.Gate;
import com.thgross.aoc2024.day24.Wire;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends Application {

    String inputFilename = "aoc2024/input24.txt";

    public static void main(String[] args) {
        var app = (new Day24());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        Map<String, Wire> wires = new HashMap<>();
        List<Wire> initWires = new ArrayList<>();
        Wire[] zWires = new Wire[63];
        List<Gate> gates = new ArrayList<>();

        Pattern pWire = Pattern.compile("^(\\w{3}):\\s+(\\d+)$");
        Pattern pGate = Pattern.compile("^(\\w{3,})\\s(AND|XOR|OR)\\s(\\w{3,})\\s->\\s(\\w{3,})$");

        for (String line : lines) {
            if (line.contains(":")) {
                // Wire
                Matcher matcher = pWire.matcher(line);
                if (matcher.find()) {
                    var w = new Wire(matcher.group(1));
                    w.value = Integer.parseInt(matcher.group(2));
                    wires.put(matcher.group(1), w);
                    initWires.add(w);
                } else {
                    throw new RuntimeException("kein Wire-Pattern: " + line);
                }
            } else if (line.contains("->")) {
                // Gate
                Matcher matcher = pGate.matcher(line);
                if (matcher.find()) {
                    var wInput1 = matcher.group(1);
                    var operation = matcher.group(2);
                    var wInput2 = matcher.group(3);
                    var wOutput = matcher.group(4);
                    if (!wires.containsKey(wInput1)) {
                        wires.put(wInput1, new Wire(wInput1));
                    }
                    if (!wires.containsKey(wInput2)) {
                        wires.put(wInput2, new Wire(wInput2));
                    }
                    if (!wires.containsKey(wOutput)) {
                        wires.put(wOutput, new Wire(wOutput));
                    }
                    var g = new Gate();
                    g.wInput1 = wires.get(wInput1);
                    g.wInput2 = wires.get(wInput2);
                    g.wOutput = wires.get(wOutput);
                    g.type = switch (operation) {
                        case "AND" -> Gate.Type.AND;
                        case "OR" -> Gate.Type.OR;
                        case "XOR" -> Gate.Type.XOR;
                        default -> throw new RuntimeException("Unknown Operation: " + operation);
                    };
                    gates.add(g);
                    g.wInput1.outputGates.add(g);
                    g.wInput2.outputGates.add(g);
                } else {
                    throw new RuntimeException("kein Wire-Pattern: " + line);
                }
            }
        }

        for (String s : wires.keySet()) {
            if (s.startsWith("z")) {
                var zIndex = Integer.parseInt(s.substring(1));
                zWires[zIndex] = wires.get(s);
            }
        }

        for (Wire initWire : initWires) {
            initWire.set(initWire.value);
        }

        System.out.println("Wires:");
        System.out.println(wires);
        System.out.println("Gates:");
        System.out.println(gates);
        System.out.println("zWired:");
        System.out.println(Arrays.toString(zWires));

        // Part 1
        long output = 0;
        for (int i = 0; i < zWires.length; i++) {
            if (zWires[i] != null) {
                output += ((long) zWires[i].value) << i;
            }
        }

        System.out.println("------------------------------------");
        System.out.printf("Output: %d\n", output);
    }
}
