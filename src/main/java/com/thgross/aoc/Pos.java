package com.thgross.aoc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Pos implements Comparable<Pos> {
    public int y, x;

    protected static final Pos[] dirs = {
            new Pos(-1, 0), // oben
            new Pos(0, 1),  // rechts
            new Pos(1, 0),  // unten
            new Pos(0, -1)  // links
    };

    public static final Pos[] around = {
            new Pos(-1, 0), // oben
            new Pos(-1, 1), // oben rechts
            new Pos(0, 1),  // rechts
            new Pos(1, 1),  // unten rechts
            new Pos(1, 0),  // unten
            new Pos(1, -1),  // unten links
            new Pos(0, -1),  // links
            new Pos(-1, -1)  // oben links
    };

    protected static final Map<Character, Integer> chardirs = new HashMap<>() {{
        put('^', 0);
        put('>', 1);
        put('v', 2);
        put('<', 3);
    }};

    public Pos() {
    }

    public Pos(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void add(Pos addPos) {
        y += addPos.y;
        x += addPos.x;
    }

    public void addDir(char c) {
        add(dirs[chardirs.get(c)]);
    }

    public Pos plus(Pos addPos) {
        return new Pos(y + addPos.y, x + addPos.x);
    }

    public Pos plus(int y, int x) {
        return new Pos(this.y + y, this.x + x);
    }

    public void sub(Pos subPos) {
        y -= subPos.y;
        x -= subPos.x;
    }

    public void move(int dir) {
        add(dirs[dir]);
    }

    public Pos minus(Pos addPos) {
        return new Pos(y - addPos.y, x - addPos.x);
    }

    public boolean isEqualTo(Pos otherPos) {
        return y == otherPos.y && x == otherPos.x;
    }

    public void clone(Pos pos) {
        y = pos.y;
        x = pos.x;
    }

    public Pos copy() {
        return new Pos(y, x);
    }

    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        Pos pos = (Pos) o;
        return y == pos.y && x == pos.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }

    @Override
    public int compareTo(Pos p2) {
        if (y < p2.y) {
            return -1;
        }
        if (y > p2.y) {
            return 1;
        }
        // ab hier ist y gleich!
        return Integer.compare(x, p2.x);
    }
}
