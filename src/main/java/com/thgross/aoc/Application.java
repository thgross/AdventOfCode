package com.thgross.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Application {

    Instant start;
    Instant stop;
    protected String inputFilename;

    protected final String ANSI_RESET = "\u001B[0m";
    protected final String ANSI_BLACK = "\u001B[30m";
    protected final String ANSI_RED = "\u001B[31m";
    protected final String ANSI_GREEN = "\u001B[32m";
    protected final String ANSI_YELLOW = "\u001B[33m";
    protected final String ANSI_BLUE = "\u001B[34m";
    protected final String ANSI_PURPLE = "\u001B[35m";
    protected final String ANSI_CYAN = "\u001B[36m";
    protected final String ANSI_WHITE = "\u001B[37m";

    protected final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    protected final String ANSI_RED_BACKGROUND = "\u001B[41m";
    protected final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    protected final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    protected final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    protected final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    protected final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    protected final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    protected final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    protected final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    protected final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    protected final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    protected final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    protected final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    protected final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    protected final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    protected static class Pos {
        public int y, x;

        public Pos() {
        }

        public Pos(int y, int x) {
            this.y = y;
            this.x = x;
        }

        public void add(Pos addPos) {
            y += addPos.y;
            x += addPos.x;
        }

        public Pos plus(Pos addPos) {
            return new Pos(y + addPos.y, x + addPos.x);
        }

        public void sub(Pos subPos) {
            y -= subPos.y;
            x -= subPos.x;
        }

        public Pos minus(Pos addPos) {
            return new Pos(y - addPos.y, x - addPos.x);
        }

        public boolean isEqualTo(Pos otherPos) {
            return y == otherPos.y && x == otherPos.x;
        }
    }

    protected final Pos[] dirs = {
            new Pos(-1, 0), // oben
            new Pos(0, 1),  // rechts
            new Pos(1, 0),  // unten
            new Pos(0, -1)  // links
    };

    protected final int TOP = 0;
    protected final int RIGHT = 1;
    protected final int BOTTOM = 2;
    protected final int LEFT = 3;

    protected Winframe winframe;

    public void run() {
    }

    public void run(String inputFilename) {
        try {
            start = Instant.now();
            calcAll(getFileLines(inputFilename));
            stop = Instant.now();

            System.out.println("======================================");
            System.out.printf("Runtime: %s\n", Duration.between(start, stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openWindow(String title, int w, int h) {
        winframe = new Winframe(title, w, h);
    }

    protected void calcAll(List<String> fileLines) throws IOException {
    }

    protected InputStream getFileAsInputStream(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filename);
    }

    protected String getFileContent(String filename) throws IOException {
        var inputStream = getFileAsInputStream(filename);
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    protected List<String> getFileLines(String filename) throws IOException {
        List<String> slines;

        try (var inputStream = getFileAsInputStream(filename)) {
            if (inputStream == null) {
                throw new RuntimeException("Stream konnte nicht geöffnet werden!");
            }

            slines = new ArrayList<>();

            try (var reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> lines = reader.lines()) {

                // Zeilenweise Verarbeitung
                lines.forEach(slines::add);
            }
        }

        return slines;
    }

    protected int[] removeElement(int[] arr, int removedIdx) {
        int[] dest = new int[arr.length - 1];

        System.arraycopy(arr, 0, dest, 0, removedIdx);
        System.arraycopy(arr, removedIdx + 1, dest, removedIdx, arr.length - 1 - removedIdx);

        return dest;
    }

    public static String trim(String input, String charsToTrim) {
        String regex = "^[" + charsToTrim + "]+|[" + charsToTrim + "]+$";
        return input.replaceAll(regex, "");
    }

    protected void printChar(char ch) {
        System.out.print(ch);
    }

    protected void printChar(char ch, String color) {
        System.out.print(color);
        printChar(ch);
        System.out.print(ANSI_RESET);
    }
}
