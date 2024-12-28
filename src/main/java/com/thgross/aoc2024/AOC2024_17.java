package com.thgross.aoc2024;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AOC2024_17 extends Application {
    public static void main(String[] args) {
        var inputfile = "aoc2024/input17.txt";
        var app = (new AOC2024_17());
        app.run(inputfile);
    }

    public static class Machine {

        public static final int REG_A = 0;
        public static final int REG_B = 1;
        public static final int REG_C = 2;

        /**
         * Division A
         */
        static final int OP_ADV = 0;
        /**
         * Bitwise XOR A
         */
        static final int OP_BXL = 1;
        /**
         * Modulo
         */
        static final int OP_BST = 2;
        /**
         * Jump
         */
        static final int OP_JNZ = 3;
        /**
         * Bitwise XOR B
         */
        static final int OP_BXC = 4;
        /**
         * Modulo Out
         */
        static final int OP_OUT = 5;
        /**
         * Division B
         */
        static final int OP_BDV = 6;
        /**
         * Division C
         */
        static final int OP_CDV = 7;


        long[] reg = new long[3];   // Registers
        int[] prog;
        int ipointer;
        List<Integer> result;

        public void setReg(char regName, long val) {
            reg[((int) regName) - 65] = val;
        }

        public List<Integer> run(int[] program) {

            prog = program;
            ipointer = 0;
            result = new ArrayList<>();

            boolean stopped;
            do {
                stopped = operate();
            } while (!stopped);

            return result;
        }

        private boolean operate() {
            int instr = prog[ipointer];
            int op = prog[ipointer + 1];

            switch (instr) {
                case OP_ADV:
                    reg[REG_A] = (int) ((double) reg[REG_A] / Math.pow(2, (double) combop(op)));
                    ipointer += 2;
                    break;

                case OP_BDV:
                    reg[REG_B] = (int) ((double) reg[REG_A] / Math.pow(2, (double) combop(op)));
                    ipointer += 2;
                    break;

                case OP_CDV:
                    reg[REG_C] = (int) ((double) reg[REG_A] / Math.pow(2, (double) combop(op)));
                    ipointer += 2;
                    break;

                case OP_BXL:
                    reg[REG_B] = reg[REG_B] ^ op;
                    ipointer += 2;
                    break;

                case OP_BST:
                    reg[REG_B] = combop(op) % 8;
                    ipointer += 2;
                    break;

                case OP_JNZ:
                    if (reg[REG_A] != 0) {
                        ipointer = op;
                    } else {
                        ipointer += 2;
                    }
                    break;

                case OP_BXC:
                    reg[REG_B] = reg[REG_B] ^ reg[REG_C];
                    ipointer += 2;
                    break;

                case OP_OUT:
                    result.add((int)(combop(op) % 8));
                    ipointer += 2;
                    break;
            }

            return ipointer >= prog.length;
        }

        private long combop(int op) {
            if (op == 4) {
                return reg[REG_A];
            } else if (op == 5) {
                return reg[REG_B];
            } else if (op == 6) {
                return reg[REG_C];
            }

            return op;
        }
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var m1 = new Machine();
        int[] program1 = new int[0];

        Pattern patternRegister = Pattern.compile("^Register (\\w): (\\d+)$");
        Pattern patternProgram = Pattern.compile("^Program: (\\d(,\\d)+)$");
        for (String line : lines) {
            Matcher matcherRegister = patternRegister.matcher(line);
            Matcher matcherProgram = patternProgram.matcher(line);
            if (matcherRegister.find()) {
                m1.setReg(matcherRegister.group(1).charAt(0), Integer.parseInt(matcherRegister.group(2)));
            } else if (matcherProgram.find()) {
                var instructions = matcherProgram.group(1).split(",");
                program1 = new int[instructions.length];
                for (int i = 0; i < instructions.length; i++) {
                    program1[i] = Integer.parseInt(instructions[i]);
                }
            }
        }

        System.out.println("----------------------------------");

        // Part 1
        var ret = m1.run(program1);
        var p1output = ret.stream().map(String::valueOf).collect(Collectors.joining(","));
        System.out.printf("Part 1 output: %s\n", p1output);

//        153240: 0,3,5,5,3,0


        // Part 2
        List<Integer> ret2;
        long p2regA = 0;
        long itstart = 0;
        long itcount = 1_000_000;
        for (long i = itstart; i < itstart + itcount; i++) {
            m1.setReg('A', i);
            m1.setReg('B', 0);
            m1.setReg('C', 0);
            ret2 = m1.run(program1);
            int[] retArray = ret2.stream().mapToInt(in -> in).toArray();

//            System.out.printf("%18d: ", i);
//            System.out.println(ret2.stream().map(String::valueOf).collect(Collectors.joining(",")));

            if (Arrays.equals(retArray, program1)) {
                p2regA = i;
                break;
            }
        }
        System.out.printf("Part 2 REG A: %d\n", p2regA);
    }
}
