package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day16 extends Application {

    String inputFilename = "aoc2015/input16.txt";

    Pattern patternName = Pattern.compile("^Sue (\\d+):");
    Pattern patternCompounds = Pattern.compile("( (\\w+): (\\d+),?)");

    public static class Sue {
        public Integer children;
        public Integer cats;
        public Integer samoyeds;
        public Integer pomeranians;
        public Integer akitas;
        public Integer vizslas;
        public Integer goldfish;
        public Integer trees;
        public Integer cars;
        public Integer perfumes;

        public static Sue build(Map<String, Integer> compounds) {
            var c = new Sue();

            compounds.forEach((compound, value) -> {
                // @Formatter:off
                switch (compound) {
                    case "children": c.children = value; break;
                    case "cats": c.cats = value; break;
                    case "samoyeds": c.samoyeds = value; break;
                    case "pomeranians": c.pomeranians = value; break;
                    case "akitas": c.akitas = value; break;
                    case "vizslas": c.vizslas = value; break;
                    case "goldfish": c.goldfish = value; break;
                    case "trees": c.trees = value; break;
                    case "cars": c.cars = value; break;
                    case "perfumes": c.perfumes = value; break;
                }
                // @Formatter:on
            });
            return c;
        }

        public String toString() {
            var sb = new StringBuilder();

            sb.append("{");
            sb.append("\"children\": ").append(children);
            sb.append(", \"cats\": ").append(cats);
            sb.append(", \"samoyeds\": ").append(samoyeds);
            sb.append(", \"pomeranians\": ").append(pomeranians);
            sb.append(", \"akitas\": ").append(akitas);
            sb.append(", \"vizslas\": ").append(vizslas);
            sb.append(", \"goldfish\": ").append(goldfish);
            sb.append(", \"trees\": ").append(trees);
            sb.append(", \"cars\": ").append(cars);
            sb.append(", \"perfumes\": ").append(perfumes);
            sb.append("}\n");

            return sb.toString();
        }

        public boolean matches(Sue readings) {
            if (this.children != null && !readings.children.equals(this.children)) return false;
            if (this.cats != null && !readings.cats.equals(this.cats)) return false;
            if (this.samoyeds != null && !readings.samoyeds.equals(this.samoyeds)) return false;
            if (this.pomeranians != null && !readings.pomeranians.equals(this.pomeranians)) return false;
            if (this.akitas != null && !readings.akitas.equals(this.akitas)) return false;
            if (this.vizslas != null && !readings.vizslas.equals(this.vizslas)) return false;
            if (this.goldfish != null && !readings.goldfish.equals(this.goldfish)) return false;
            if (this.trees != null && !readings.trees.equals(this.trees)) return false;
            if (this.cars != null && !readings.cars.equals(this.cars)) return false;
            if (this.perfumes != null && !readings.perfumes.equals(this.perfumes)) return false;

            return true;
        }

        public boolean matchesPart2(Sue readings) {
            if (this.children != null && this.children != readings.children) return false;
            if (this.cats != null && this.cats <= readings.cats) return false;
            if (this.samoyeds != null && this.samoyeds != readings.samoyeds) return false;
            if (this.pomeranians != null && this.pomeranians >= readings.pomeranians) return false;
            if (this.akitas != null && this.akitas != readings.akitas) return false;
            if (this.vizslas != null && this.vizslas != readings.vizslas) return false;
            if (this.goldfish != null && this.goldfish >= readings.goldfish) return false;
            if (this.trees != null && this.trees <= readings.trees) return false;
            if (this.cars != null && this.cars != readings.cars) return false;
            if (this.perfumes != null && this.perfumes != readings.perfumes) return false;

            return true;
        }
    }

    public static void main(String[] args) {
        var app = (new Day16());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        Sue cardSue = Sue.build(Map.of(
                "children", 3,
                "cats", 7,
                "samoyeds", 2,
                "pomeranians", 3,
                "akitas", 0,
                "vizslas", 0,
                "goldfish", 5,
                "trees", 3,
                "cars", 2,
                "perfumes", 1
        ));
        List<Sue> sues = new ArrayList<>();

        for (String line : lines) {
            var matcherName = patternName.matcher(line);
            var matcherCompounds = patternCompounds.matcher(line);
            var compounds = new HashMap<String, Integer>();
            while (matcherCompounds.find()) {
                compounds.put(matcherCompounds.group(2), Integer.valueOf(matcherCompounds.group(3)));
            }
            sues.add(Sue.build(compounds));
        }

        System.out.println(sues);

        Integer part1SueNr = null;
        Integer part2SueNr = null;

        // Part 1
        for (int i = 0; i < sues.size(); i++) {
            if (sues.get(i).matches(cardSue)) {
                part1SueNr = i + 1;
            }
        }
        // Part 2
        for (int i = 0; i < sues.size(); i++) {
            if (sues.get(i).matchesPart2(cardSue)) {
                part2SueNr = i + 1;
            }
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 Sue-Nr: %d\n", part1SueNr);
        System.out.printf("Part 2 Sue-Nr: %d\n", part2SueNr);
    }
}
