package com.thgross.aoc2024.day21;

import com.thgross.aoc.Pos;

public class Numpad extends Pad {
    public Numpad() {

        buttons.put('7', new Pos(0, 0));
        buttons.put('8', new Pos(0, 1));
        buttons.put('9', new Pos(0, 2));
        buttons.put('4', new Pos(1, 0));
        buttons.put('5', new Pos(1, 1));
        buttons.put('6', new Pos(1, 2));
        buttons.put('1', new Pos(2, 0));
        buttons.put('2', new Pos(2, 1));
        buttons.put('3', new Pos(2, 2));
        buttons.put('0', new Pos(3, 1));
        buttons.put('A', new Pos(3, 2));

        currentButton = 'A';

        init();
    }
}
