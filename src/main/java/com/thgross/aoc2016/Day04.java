package com.thgross.aoc2016;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 extends Application {

    String inputFilename = "aoc2016/input04.txt";

    static class Room {
        String namesRaw;
        String namesRawShifted;
        Map<Character, Integer> charcounts;
        List<Map.Entry<Character, Integer>> charlist;
        Integer sectorId;
        String calcChecksum;
        String checksum;

        public static Room create(Matcher matcher) {
            var room = new Room();
            room.namesRaw = matcher.group(1);
            room.sectorId = Integer.parseInt(matcher.group(3));
            room.checksum = matcher.group(4);
            room.init();
            return room;
        }

        @Override
        public String toString() {
            return String.format("sectorId: %d\n\tnamesRaw: %s\n\tnamesRawShifted: %s", sectorId, namesRaw, namesRawShifted);
        }

        private void init() {
            charcounts = new HashMap<>();
            for (char c : namesRaw.toCharArray()) {
                if (c == '-') {
                    continue;
                }
                if (charcounts.containsKey(c)) {
                    charcounts.put(c, charcounts.get(c) + 1);

                } else {
                    charcounts.put(c, 1);
                }
            }

            charlist = new ArrayList<>(charcounts.entrySet());
            charlist.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return Integer.signum(o2.getValue() - o1.getValue());
                } else {
                    return Integer.signum(o1.getKey() - o2.getKey());
                }
            });

            var checksumBuilder = new StringBuilder();
            for (int i = 0; i < charlist.size() && i < 5; i++) {
                checksumBuilder.append(charlist.get(i).getKey());
            }
            calcChecksum = checksumBuilder.toString();

            shiftNamesRaw();
        }

        private void shiftNamesRaw() {
            var shifted = new StringBuilder();
            for (char c : namesRaw.toCharArray()) {
                if (c == '-') {
                    shifted.append(shiftChar(c, sectorId, '-', 2));
                } else {
                    shifted.append(shiftChar(c, sectorId, 'a', 26));
                }
            }
            namesRawShifted = shifted.toString();
        }

        private char shiftChar(char c, int offset, char baseChar, int modulo) {
            return (char) ((((int) c - (int) baseChar + offset) % modulo) + (int) baseChar);
        }

        public boolean isReal() {
            return Objects.equals(checksum, calcChecksum);
        }
    }

    public static void main(String[] args) {
        var app = (new Day04());
        app.run(app.inputFilename);
    }

    protected void calcAll(List<String> lines) throws IOException {

        Long idSum = 0L;
        Integer idLocationPart2 = 0;
        List<Room> rooms = new ArrayList<>();

        var pattern = Pattern.compile("^(((?:[a-z]+)-?)+)-(\\d+)\\[(\\w+)]$");

        for (String line : lines) {
            var matcher = pattern.matcher(line);
            if (matcher.find()) {
                var room = Room.create(matcher);
                if (room.isReal()) {
                    idSum += room.sectorId;
                }
                if (room.namesRawShifted.contains("pole")) {
                    System.out.printf("%s\n", room.toString());
                    idLocationPart2 = room.sectorId;
                }
                rooms.add(room);
            }
        }

        System.out.printf("Part 1 Sum of Sector IDs: %d\n", idSum);
        System.out.printf("Part 1 Sector ID: %d\n", idLocationPart2);
//        System.out.printf("Part 2 real Triangles: %d\n", numRealTrianglesPart2);
    }
}