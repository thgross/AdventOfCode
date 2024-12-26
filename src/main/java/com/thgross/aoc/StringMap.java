package com.thgross.aoc;

import java.util.Arrays;

public class StringMap {
    public String[][] smap;

    private StringMap() {
    }

    public StringMap(int rows, int cols) {
        smap = new String[rows][cols];
    }

    public StringMap(int rows, int cols, String initString) {
        smap = new String[rows][cols];
        for (String[] strings : smap) {
            Arrays.fill(strings, initString);
        }
    }

    public void dump() {
        for (String[] strings : smap) {
            StringBuilder sbRow = new StringBuilder();
            for (String string : strings) {
                sbRow.append(string);
            }

            System.out.println(sbRow);
        }
    }
}
