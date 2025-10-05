package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

public class Day05 extends Application {

    String inputFilename = "aoc2016/input05.txt";

    public static void main(String[] args) {
        var app = (new Day05());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        String doorIdBase = lines.getFirst();
        StringBuilder p1Password = new StringBuilder();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        int pwCharCount = 0;
        long index = 0;
        byte[] doorId;
        byte[] doorIdMD5;
        String doorIdMD5Hex;
        while (pwCharCount < 8) {
            doorId = (doorIdBase + index).getBytes(StandardCharsets.US_ASCII);
            doorIdMD5 = md.digest(doorId);
            doorIdMD5Hex = HexFormat.of().formatHex(doorIdMD5);
//            if ((index % 1000000) == 0 || index == 3231929) {
//                System.out.printf(
//                        "Index: %d | pwCharCount: %d | doorId: %s | doorIdMD5Hex: %s | p1Password: %s\n",
//                        index,
//                        pwCharCount,
//                        new String(doorId, StandardCharsets.US_ASCII),
//                        doorIdMD5Hex,
//                        p1Password
//                );
//            }
            index++;

            if (doorIdMD5Hex.startsWith("00000")) {
                p1Password.append(doorIdMD5Hex.charAt(5));
                pwCharCount++;
            }
        }


        System.out.printf("Part 1 Password: %s\n", p1Password);
//        System.out.printf("Part 1 Sector ID: %d\n", idLocationPart2);
    }
}