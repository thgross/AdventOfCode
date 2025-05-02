package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day15 extends Application {

    String inputFilename = "aoc2015/input15.txt";

    Pattern patternLine = Pattern.compile("^(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)$");

    static record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {
    }

    public static void main(String[] args) {
        var app = (new Day15());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var ingredients = new ArrayList<Ingredient>();

        for (String line : lines) {
            var matcher = patternLine.matcher(line);
            while (matcher.find()) {
                var name = matcher.group(1);
                var capacity = matcher.group(2);
                var durability = matcher.group(3);
                var flavor = matcher.group(4);
                var texture = matcher.group(5);
                var calories = matcher.group(6);

                var ingredient = new Ingredient(
                        name,
                        Integer.parseInt(capacity),
                        Integer.parseInt(durability),
                        Integer.parseInt(flavor),
                        Integer.parseInt(texture),
                        Integer.parseInt(calories));
                ingredients.add(ingredient);
            }
        }

        System.out.println(ingredients);

        var kombinationen = berechneKombinationen(ingredients.size(), 100, 1, 100);
//        System.out.println(kombinationen);

        var part1BestScore = Long.MIN_VALUE;
        var part1BestKombi = Integer.MIN_VALUE;
        var part2BestScore = Long.MIN_VALUE;
        var part2BestKombi = Integer.MIN_VALUE;
        // Part 1 / Part 2
        for (int k = 0; k < kombinationen.size(); k++) {
            var capacity = 0L;
            var durability = 0L;
            var flavor = 0L;
            var texture = 0L;
            var calories = 0L;

            for (int i = 0; i < kombinationen.get(k).size(); i++) {
                capacity += (long) ingredients.get(i).capacity * kombinationen.get(k).get(i);
                durability += (long) ingredients.get(i).durability * kombinationen.get(k).get(i);
                flavor += (long) ingredients.get(i).flavor * kombinationen.get(k).get(i);
                texture += (long) ingredients.get(i).texture * kombinationen.get(k).get(i);
                calories += (long) ingredients.get(i).calories * kombinationen.get(k).get(i);
            }

            var score = (capacity > 0 ? capacity : 0) * (durability > 0 ? durability : 0) * (flavor > 0 ? flavor : 0) * (texture > 0 ? texture : 0);
            if(score > part1BestScore) {
                part1BestScore = score;
                part1BestKombi = k;
            }
            if(calories == 500 && score > part2BestScore) {
                part2BestScore = score;
                part2BestKombi = k;
            }
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.println(kombinationen.get(part1BestKombi));
        System.out.printf("Part 1 Best Kombi-ID: %d best score: %d\n", part1BestKombi, part1BestScore);
        System.out.printf("Part 2 Best Kombi-ID: %d best score: %d\n", part2BestKombi, part2BestScore);
    }

    public static List<List<Integer>> berechneKombinationen(int anzahlEigenschaften, int zielSumme, int minWertProEigenschaft, int maxWertProEigenschaft) {
        List<List<Integer>> ergebnisse = new ArrayList<>();
        kombiniere(ergebnisse, new ArrayList<>(), anzahlEigenschaften, zielSumme, minWertProEigenschaft, maxWertProEigenschaft);
        return ergebnisse;
    }

    private static void kombiniere(List<List<Integer>> ergebnisse, List<Integer> aktuelleKombination,
                                   int verbleibend, int restSumme, int minWertProEigenschaft, int maxWertProEigenschaft) {
        if (verbleibend == 1) {
            if (restSumme >= minWertProEigenschaft && restSumme <= maxWertProEigenschaft) {
                List<Integer> fertigeKombi = new ArrayList<>(aktuelleKombination);
                fertigeKombi.add(restSumme);
                ergebnisse.add(fertigeKombi);
            }
            return;
        }

        for (int i = minWertProEigenschaft; i <= Math.min(maxWertProEigenschaft, restSumme); i++) {
            aktuelleKombination.add(i);
            kombiniere(ergebnisse, aktuelleKombination, verbleibend - 1, restSumme - i, 1, maxWertProEigenschaft);
            aktuelleKombination.removeLast(); // Backtracking
        }
    }
}
