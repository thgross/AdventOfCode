package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import com.thgross.aoc.Point3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day08 extends Application {

    String inputFilename = "aoc2025/input08.txt";

    static void main() {
        var app = (new Day08());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var closestPairsLimit = 1000;
        var largestCircuitCount = 3;

        long part1CircuitMult = 1L;

        List<Box> boxes = new ArrayList<>();

        for (String line : lines) {
            var lineParts = line.split(",");
            boxes.add(new Box(Double.parseDouble(lineParts[0]), Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2])));
        }

//        System.out.println(boxes);

        var shortestConnections = getTopClosestBoxPairs(boxes, closestPairsLimit);

//        System.out.println(shortestConnections);

        List<Circuit> circuits = new ArrayList<>();

        @SuppressWarnings("unused")
        int i = -1;
        for (BoxPair conn : shortestConnections) {
            i++;
            if (conn.b1.circuit != null && (conn.b1.circuit.equals(conn.b2.circuit))) {
                // Bereits im gleichen Circuit
                continue;
            }

            Circuit circuit;
            boolean circuitIsNew = false;

            if (conn.b1.circuit == null) {
                circuit = new Circuit();
                circuitIsNew = true;
                circuit.addBox(conn.b1);
            } else {
                circuit = conn.b1.circuit;
            }
            // Alle Boxen aus conn.b2.circuit zum conn.b1.circuit packen
            if (conn.b2.circuit == null) {
                circuit.addBox(conn.b2);
            } else {
                var b2Circuit = conn.b2.circuit;
                for (Box boxC2 : conn.b2.circuit.boxes) {
                    circuit.addBox(boxC2);
                }
                circuits.remove(b2Circuit);
            }

            if (circuitIsNew) {
                circuits.add(circuit);
            }

//            System.out.println(String.format("*** Iteration %d:", i));
//            System.out.println(circuits);
        }

        circuits.sort((c1, c2) -> c2.boxes.size() - c1.boxes.size());

        for(int p1I = 0; p1I < largestCircuitCount; p1I++) {
            part1CircuitMult *= circuits.get(p1I).boxes.size();
        }

//        System.out.println(circuits);

        System.out.println("------------------------------------");
//        dumpCharMap(map, colorMap);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Circuit Sizes Mult: %d\n", part1CircuitMult);
//        System.out.printf("Part 2 Paths: %d\n", part2Paths);
    }

    public List<BoxPair> getTopClosestBoxPairs(List<Box> points, int limit) {
        List<BoxPair> allBoxPairs = new ArrayList<>();

        // Alle Paare durchlaufen (ohne Dopplungen und ohne Punkt mit sich selbst)
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                allBoxPairs.add(new BoxPair(points.get(i), points.get(j)));
            }
        }

        // Sortieren und die ersten 10 nehmen
        return allBoxPairs.stream()
                .sorted()
                .limit(limit)
                .toList();
    }

    public static class Box {
        Point3D pos;
        Circuit circuit;

        public Box(double x, double y, double z) {
            pos = new Point3D(x, y, z);
            circuit = null;
        }

        public String toString() {
            return String.format("(%.0f, %.0f, %.0f)", pos.x, pos.y, pos.z);
        }
    }

    public static class BoxPair implements Comparable<BoxPair> {
        Box b1, b2;
        double distanceSq;

        public BoxPair(Box b1, Box b2) {
            this.b1 = b1;
            this.b2 = b2;
            this.distanceSq = b1.pos.distanceSquared(b2.pos);
        }

        @Override
        public int compareTo(BoxPair other) {
            return Double.compare(this.distanceSq, other.distanceSq);
        }

        @Override
        public String toString() {
            return String.format("[dist:%.0f (%.0f, %.0f, %.0f),(%.0f, %.0f, %.0f)]", distanceSq, b1.pos.x, b1.pos.y, b1.pos.z, b2.pos.x, b2.pos.y, b2.pos.z);
        }
    }

    public static class Circuit {
        public List<Box> boxes;

        public Circuit() {
            boxes = new ArrayList<>();
        }

        public void addBox(Box box) {
            boxes.add(box);
            box.circuit = this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("\t[[ %d: ", boxes.size()));
            for (Box box : boxes) {
                sb.append(box.toString());
            }
            sb.append(" ]]\n");
            return sb.toString();
        }
    }
}
