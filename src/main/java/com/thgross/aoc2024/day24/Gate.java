package com.thgross.aoc2024.day24;

import com.thgross.aoc.Application;

public class Gate {
    public Wire wInput1;
    public Wire wInput2;
    public Wire wOutput;
    public Integer value = null;
    public Type type;

    public enum Type {AND, OR, XOR}

    public void wireUpdated() {
        if (wInput1 != null && wInput1.value != null && wInput2 != null && wInput2.value != null) {
            calc(wInput1.value, wInput2.value);
        }
    }

    public void calc(Integer val1, Integer val2) {
        // TODO: calc
        value = switch (type) {
            case AND -> val1 & val2;
            case OR -> val1 | val2;
            case XOR -> val1 ^ val2;
        };

        wOutput.set(value);
    }

    @Override
    public String toString() {
        return "{" + Application.BLUE_BRIGHT + wInput1.name + Application.RESET
                + " " + Application.YELLOW + type + Application.RESET
                + " " + Application.BLUE_BRIGHT + wInput2.name + Application.RESET
                + " -> "
                + Application.PURPLE_BRIGHT + wOutput.name + Application.RESET
                + "}";
    }
}
