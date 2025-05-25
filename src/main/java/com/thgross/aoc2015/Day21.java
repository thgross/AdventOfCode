package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day21 extends Application {

    String inputFilename = "aoc2015/input21.txt";

    Pattern patternCategory = Pattern.compile("^(\\w+):\\s+Cost\\s+Damage\\s+Armor$");
    Pattern patternItem = Pattern.compile("^(\\w+( \\+\\d)?)\\s{2,}+(\\d+)\\s+(\\d+)\\s+(\\d+)$");
    Pattern patternBossFeature = Pattern.compile("^(.*):\\s+(.*)$");

    static class Fighter {
        int hitpoints;
        int damage;
        int armor;

        public void hit(Fighter opponent) {
            opponent.hitpoints -= Math.max(1, this.damage - opponent.armor);
        }

        public boolean isAlive() {
            return this.hitpoints > 0;
        }
    }

    static class Category {
        String name;
        List<Item> items;

        @Override
        public String toString() {
            return "Category{" +
                    "\n\tname='" + name + '\'' +
                    ",\n\titems=" + items +
                    "\n}";
        }
    }

    static class Item {
        String name;
        Integer cost;
        Integer damage;
        Integer armor;

        @Override
        public String toString() {
            return "Item{" +
                    "\n\tname='" + name + '\'' +
                    ", cost=" + cost +
                    ", damage=" + damage +
                    ", armor=" + armor +
                    '}';
        }
    }

    public static void main(String[] args) {
        var app = (new Day21());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var player = new Fighter();
        var bossStartValues = new Fighter();
        for (String bossFeatureLine : lines) {
            var matcherBossFeature = patternBossFeature.matcher(bossFeatureLine);
            if (matcherBossFeature.find()) {
                switch (matcherBossFeature.group(1)) {
                    case "Hit Points":
                        bossStartValues.hitpoints = Integer.parseInt(matcherBossFeature.group(2));
                        break;
                    case "Damage":
                        bossStartValues.damage = Integer.parseInt(matcherBossFeature.group(2));
                        break;
                    case "Armor":
                        bossStartValues.armor = Integer.parseInt(matcherBossFeature.group(2));
                        break;
                }
            }
        }

        var shopLines = getFileLines("aoc2015/input21shop.txt");

        String catName = null;

        Map<String, Category> shop = new HashMap<>();
        for (String shopLine : shopLines) {
            var matcherCategory = patternCategory.matcher(shopLine);
            if (matcherCategory.find()) {
                catName = matcherCategory.group(1);
                var cat = new Category();
                cat.name = catName;
                cat.items = new ArrayList<>();
                shop.put(catName, cat);
            }
            var matcherItem = patternItem.matcher(shopLine);
            if (matcherItem.find()) {
                var item = new Item();
                item.name = matcherItem.group(1);
                item.cost = Integer.valueOf(matcherItem.group(3));
                item.damage = Integer.valueOf(matcherItem.group(4));
                item.armor = Integer.valueOf(matcherItem.group(5));
                shop.get(catName).items.add(item);
            }
        }

        System.out.println(bossStartValues);
        System.out.println(shop);

        // Part 1
        var boss = new Fighter();
        int costMinimum = 999999999;
        int costMinimumFight = 0;
        int costMaximum = -999999999;
        int costMaximumFight = 0;
        int costCurrent = 0;
        int fightCount = 0;
        List<Item> pitems;

        // Weapons (must use one)
        for (int w = 0; w < shop.get("Weapons").items.size(); w++) {
            // Armor (zero or one; a=-1 means no Armor)
            for (int a = -1; a < shop.get("Armor").items.size(); a++) {
                // Ring 1
                for (int r1 = -1; r1 < shop.get("Rings").items.size(); r1++) {
                    // Ring 2
                    for (int r2 = -1; r2 < shop.get("Rings").items.size(); r2++) {
                        if (r2 == r1) {
                            continue;
                        }

                        pitems = new ArrayList<>();
                        pitems.add(shop.get("Weapons").items.get(w));
                        if (a >= 0) {
                            pitems.add(shop.get("Armor").items.get(a));
                        }
                        if (r1 >= 0) {
                            pitems.add(shop.get("Rings").items.get(r1));
                        }
                        if (r2 >= 0) {
                            pitems.add(shop.get("Rings").items.get(r2));
                        }

                        boss.hitpoints = bossStartValues.hitpoints;
                        boss.damage = bossStartValues.damage;
                        boss.armor = bossStartValues.armor;

                        player.hitpoints = 100;
                        player.damage = 0;
                        player.armor = 0;

                        costCurrent = 0;

                        for (Item pitem : pitems) {
                            costCurrent += pitem.cost;
                            player.damage += pitem.damage;
                            player.armor += pitem.armor;
                        }

                        int halbrunde = 0;

                        fightCount++;

                        while (player.isAlive() && boss.isAlive()) {
                            if (halbrunde % 2 == 0) {
                                player.hit(boss);
                            } else {
                                boss.hit(player);
                            }

                            halbrunde++;
                        }

                        if (player.isAlive()) {
                            if (costCurrent < costMinimum) {
                                costMinimum = costCurrent;
                                costMinimumFight = fightCount;
                            }
                        }
                        if (boss.isAlive()) {
                            if (costCurrent > costMaximum) {
                                costMaximum = costCurrent;
                                costMaximumFight = fightCount;
                            }
                        }

                        // TODO: Calc
                    }
                }
            }
        }

        // Part 1


        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("Part 1 | checked %d fights: Minimal Win Cost: %d (Fight %d)\n", fightCount, costMinimum, costMinimumFight);
        System.out.printf("Part 2 | checked %d fights: Maximal Lose Cost: %d (Fight %d)\n", fightCount, costMaximum, costMaximumFight);
    }
}
