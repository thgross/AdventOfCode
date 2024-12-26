package com.thgross.day21;

import com.thgross.aoc.Pos;

public class Dirpad extends Pad {
    public Dirpad() {
        buttons.put('^', new Pos(0, 1));
        buttons.put('A', new Pos(0, 2));
        buttons.put('<', new Pos(1, 0));
        buttons.put('v', new Pos(1, 1));
        buttons.put('>', new Pos(1, 2));

        currentButton = 'A';

        init();
    }
}
