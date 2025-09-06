package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;

public class Day22 extends Application {

    String inputFilename = "aoc2015/input22.txt";
    List<Spell> allSpells;
    Stack<Spell> activeSpells;
    Stack<State> states;
    Map<Spell, Integer> activeSpellsTurnsLeft;
    Fighter player;
    Fighter boss;

    static class State {
        Set<Spell> activeSpells;
        Fighter player;
        Fighter boss;

        public State(State previousState) {
            this.activeSpells = new HashSet<>();
            this.activeSpells.addAll(previousState.activeSpells);
            this.player = new Fighter(previousState.player);
            this.boss = new Fighter(previousState.boss);
        }
    }

    static class Spell {
        String name;
        int manaCost;
        int turns;
        int plusHitpoints;
        int plusArmor;
        int plusMana;
        int dealsDamage;

        public Spell(String name, int manaCost, int turns, int plusHitpoints, int plusArmor, int plusMana, int dealsDamage) {
            this.name = name;
            this.manaCost = manaCost;
            this.turns = turns;
            this.plusHitpoints = plusHitpoints;
            this.plusArmor = plusArmor;
            this.plusMana = plusMana;
            this.dealsDamage = dealsDamage;
        }
    }

    static class Fighter {
        int hitpoints;
        int manapoints;
        int armor;
        int damage;

        public Fighter(Fighter fighter) {
            this.hitpoints = fighter.hitpoints;
            this.manapoints = fighter.manapoints;
            this.armor = fighter.armor;
            this.damage = fighter.damage;
        }

        public Fighter(int hitpoints, int manapoints, int armor, int damage) {
            this.hitpoints = hitpoints;
            this.manapoints = manapoints;
            this.armor = armor;
            this.damage = damage;
        }

        public boolean isAlive() {
            return this.hitpoints > 0;
        }

        public void hit(Fighter opponent) {
            opponent.hitpoints -= Math.max(1, this.damage - opponent.armor);
        }

        public void castSpellDamage(Spell spell) {
        }

        public void castSpellBuff(Spell spell) {
        }
    }

    public static void main(String[] args) {
        var app = (new Day22());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        player = new Fighter(50, 500, 0, 0);
        boss = new Fighter(
                Integer.parseInt(extract(lines.get(0), "^Hit Points: (\\d+)$")),
                0,
                0,
                Integer.parseInt(extract(lines.get(1), "^Damage: (\\d+)$"))
        );

        allSpells = new ArrayList<Spell>();
        allSpells.add(new Spell("Magic Missile", 53, 0, 0, 0, 0, 4));
        allSpells.add(new Spell("Drain", 73, 0, 2, 0, 0, 2));
        allSpells.add(new Spell("Shield", 113, 6, 0, 7, 0, 0));
        allSpells.add(new Spell("Poison", 173, 6, 0, 0, 0, 3));
        allSpells.add(new Spell("Recharge", 229, 5, 0, 0, 101, 0));

        states = new Stack<>();
        activeSpells = new Stack<>();
        activeSpellsTurnsLeft = new HashMap<>();

        // Part 1
//        while (player.isAlive() && boss.isAlive()) {
//        }

        System.out.println("----------------------------------------------------------------------------");
        // System.out.printf("Part 1 | checked %d fights: Minimal Win Cost: %d (Fight %d)\n", fightCount, costMinimum, costMinimumFight);
        // System.out.printf("Part 2 | checked %d fights: Maximal Lose Cost: %d (Fight %d)\n", fightCount, costMaximum, costMaximumFight);
    }

    protected void turn(int turnNr) {

        // TODO: alte Werte merken

        for (Spell spell : allSpells) {

            // 1. Player
            // Spell noch nicht aktiv?
            if (!activeSpells.contains(spell)) {
                activeSpells.push(spell);
                activeSpellsTurnsLeft.put(spell, spell.turns);

                player.castSpellBuff(spell);
                boss.castSpellDamage(spell);

                // TODO: check if boss is dead

            } else {
                continue;
            }

            // 2. Boss
            boss.hit(player);

            // TODO: check if player is dead

            turn(turnNr++);

            activeSpells.pop();
            activeSpellsTurnsLeft.remove(spell);
        }

        // TODO: alte Werte wiederherstellen
    }
}
