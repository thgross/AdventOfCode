package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AOC2024_15 extends Application {
    public static void main(String[] args) {
        (new AOC2024_15()).run("input15-t1.txt");
    }

    static class Pdata {
        int mapW, mapH;
        char[][] map;
        List<Integer> robotRules = new ArrayList<>();
        Instant start;
        Instant stop;
    }

    private final AOC2024_15.Pdata lc = new AOC2024_15.Pdata();

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> slines) throws IOException {

        // TODO: calculate map size and read map

//        lc.mapH = slines.size();
//        lc.mapW= slines.getFirst().length();
//        lc.map = new char[lc.mapH][lc.mapW];

        // TODO: fill map and robot rules

    }
}
