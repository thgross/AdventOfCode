package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AOC2024_13 extends Application {
    public static void main(String[] args) {
        (new AOC2024_13()).run();
    }

    static class Pos {
        int x, y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Machine {
        Pos A;
        Pos B;
        Pos price;

        public Machine(int ax, int ay, int bx, int by, int priceX, int priceY) {
            A = new Pos(ax, ay);
            B = new Pos(bx, by);
            this.price = new Pos(priceX, priceY);
        }
    }

    static class Pdata {
        List<Machine> machines = new ArrayList<>();
        int costA = 3;
        int costB = 1;
        Instant start;
        Instant stop;
    }

    private final AOC2024_13.Pdata lc = new AOC2024_13.Pdata();

    @Override
    public void run() {
        try {
            lc.start = Instant.now();
            calcAll("input13-t1.txt");
            lc.stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(lc.start, lc.stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var content = getFileContent(inputFile);

        var pattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)$\\nButton B: X\\+(\\d+), Y\\+(\\d+$)\\nPrize: X=(\\d+), Y=(\\d+)$", Pattern.DOTALL | Pattern.MULTILINE);

        var matcher = pattern.matcher(content);

        while (matcher.find()) {
            var gc = matcher.groupCount();
            lc.machines.add(new Machine(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(6))
            ));
        }

//        System.out.printf("Part 1 Price: %d\n", price1);
//        System.out.printf("Part 2 Price: %d\n", price2);
    }
}
