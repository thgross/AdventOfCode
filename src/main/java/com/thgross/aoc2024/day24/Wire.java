package com.thgross.aoc2024.day24;

import com.thgross.aoc.Application;

import java.util.ArrayList;
import java.util.List;

public class Wire {
    public String name = null;
    public Integer value = null;
    public List<Gate> outputGates = new ArrayList<>();

    public Wire(String name) {
        this.name = name;
    }

    public void set(Integer val) {
        value = val;
        if (value != null) {
            for (Gate outputGate : outputGates) {
                outputGate.wireUpdated();
            }
        }
    }

    @Override
    public String toString() {
        return value == 0 ? Application.RED + value + Application.RESET : Application.GREEN_BRIGHT + value + Application.RESET;
    }
}
