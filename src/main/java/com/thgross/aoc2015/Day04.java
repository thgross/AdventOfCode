package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

public class Day04 extends Application {

    public static void main(String[] args) {
        var app = (new Day04());
        app.run(null);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        // Part 1
        int p1answer = 0;

        String secretKey = "iwrupvqb";
        int postfix = 609043;
        byte[] bytes;
        String hexString;

        MessageDigest md;
        var hex = HexFormat.of();
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Part 1
        int pf = 0;
        while (true) {
            bytes = (secretKey + pf).getBytes();
            md.update(bytes);
            hexString = hex.formatHex(md.digest());
            if (hexString.startsWith("000000")) {
                break;
            }
            pf++;
        }
        p1answer = pf;
        System.out.println(hexString);

        System.out.println("------------------------------------");
        System.out.printf("Part 1 number: %d\n", p1answer);
    }
}
