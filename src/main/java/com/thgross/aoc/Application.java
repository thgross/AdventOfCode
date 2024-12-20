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
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Application {

    Instant start;
    Instant stop;
    protected String inputFilename;

    // https://stackoverflow.com/a/45444716/4576064

    protected final String RESET = "\u001B[0m";
    protected final String BLACK = "\u001B[30m";
    protected final String RED = "\u001B[31m";
    protected final String GREEN = "\u001B[32m";
    protected final String YELLOW = "\u001B[33m";
    protected final String BLUE = "\u001B[34m";
    protected final String PURPLE = "\u001B[35m";
    protected final String CYAN = "\u001B[36m";
    protected final String WHITE = "\u001B[37m";
    protected final String BLACK_BRIGHT = "\033[0;90m";
    protected final String RED_BRIGHT = "\033[0;91m";
    protected final String GREEN_BRIGHT = "\033[0;92m";
    protected final String YELLOW_BRIGHT = "\033[0;93m";
    protected final String BLUE_BRIGHT = "\033[0;94m";
    protected final String PURPLE_BRIGHT = "\033[0;95m";
    protected final String CYAN_BRIGHT = "\033[0;96m";
    protected final String WHITE_BRIGHT = "\033[0;97m";

    protected final String BG_BLACK = "\u001B[40m";
    protected final String BG_RED = "\u001B[41m";
    protected final String BG_GREEN = "\u001B[42m";
    protected final String BG_YELLOW = "\u001B[43m";
    protected final String BG_BLUE = "\u001B[44m";
    protected final String BG_PURPLE = "\u001B[45m";
    protected final String BG_CYAN = "\u001B[46m";
    protected final String BG_WHITE = "\u001B[47m";
    protected final String BG_BLACK_BRIGHT = "\033[0;100m";// BLACK
    protected final String BG_RED_BRIGHT = "\033[0;101m";// RED
    protected final String BG_GREEN_BRIGHT = "\033[0;102m";// GREEN
    protected final String BG_YELLOW_BRIGHT = "\033[0;103m";// YELLOW
    protected final String BG_BLUE_BRIGHT = "\033[0;104m";// BLUE
    protected final String BG_PURPLE_BRIGHT = "\033[0;105m"; // PURPLE
    protected final String BG_CYAN_BRIGHT = "\033[0;106m";  // CYAN
    protected final String BG_WHITE_BRIGHT = "\033[0;107m";   // WHITE

    protected static class Pos implements Comparable<Pos> {
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

        public Pos plus(int y, int x) {
            return new Pos(this.y + y, this.x + x);
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

        public void clone(Pos pos) {
            y = pos.y;
            x = pos.x;
        }

        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            Pos pos = (Pos) o;
            return y == pos.y && x == pos.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x);
        }

        @Override
        public int compareTo(Pos p2) {
            if (y < p2.y) {
                return -1;
            }
            if (y > p2.y) {
                return 1;
            }
            // ab hier ist y gleich!
            return Integer.compare(x, p2.x);
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
        System.out.print(RESET);
    }

    protected void printChar(char ch, String color, String bgcolor) {
        System.out.print(bgcolor);
        printChar(ch, color);
    }
}
