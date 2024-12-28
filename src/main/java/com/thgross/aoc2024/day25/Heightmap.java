package com.thgross.aoc2024.day25;

import java.util.Arrays;
import java.util.List;

public class Heightmap {
    int[] heights = new int[5];

    public Heightmap() {
        Arrays.fill(heights, 0);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "heights=" + Arrays.toString(heights) +
                '}';
    }

    public boolean fits(Heightmap hm2) {
        for (int i = 0; i < heights.length; i++) {
            if (hm2.heights[i] + heights[i] > 5) {
                return false;
            }
        }
        return true;
    }

    public static Heightmap fromHMLines(List<String> hmlines) {
        Heightmap hm = null;
        for (int i = 0; i < hmlines.size() - 1; i++) {
            var hmline = hmlines.get(i);
            if (i == 0) {
                if (hmline.equals("#####")) {
                    hm = new Lock();
                } else {
                    hm = new Key();
                }
            } else {
                for (int i1 = 0; i1 < hmline.length(); i1++) {
                    if (hmline.charAt(i1) == '#') {
                        hm.heights[i1]++;
                    }
                }
            }
        }

        return hm;
    }
}
