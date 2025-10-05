package com.thgross.aoc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class Application {

    Instant start;
    Instant stop;
    protected String inputFilename;

    // https://stackoverflow.com/a/45444716/4576064

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLACK_BRIGHT = "\033[0;90m";
    public static final String RED_BRIGHT = "\033[0;91m";
    public static final String GREEN_BRIGHT = "\033[0;92m";
    public static final String YELLOW_BRIGHT = "\033[0;93m";
    public static final String BLUE_BRIGHT = "\033[0;94m";
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String CYAN_BRIGHT = "\033[0;96m";
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String BLACK_BRIGHT2 = "\033[1;90m";
    public static final String RED_BRIGHT2 = "\033[1;91m";
    public static final String GREEN_BRIGHT2 = "\033[1;92m";
    public static final String YELLOW_BRIGHT2 = "\033[1;93m";
    public static final String BLUE_BRIGHT2 = "\033[1;94m";
    public static final String PURPLE_BRIGHT2 = "\033[1;95m";
    public static final String CYAN_BRIGHT2 = "\033[1;96m";
    public static final String WHITE_BRIGHT2 = "\033[1;97m";

    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";
    public static final String BG_BLACK_BRIGHT = "\033[0;100m";// BLACK
    public static final String BG_RED_BRIGHT = "\033[0;101m";// RED
    public static final String BG_GREEN_BRIGHT = "\033[0;102m";// GREEN
    public static final String BG_YELLOW_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BG_BLUE_BRIGHT = "\033[0;104m";// BLUE
    public static final String BG_PURPLE_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String BG_CYAN_BRIGHT = "\033[0;106m";  // CYAN
    public static final String BG_WHITE_BRIGHT = "\033[0;107m";   // WHITE

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

    protected Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public void run() {
    }

    public void run(String inputFilename) {
        try {
            System.out.println(this.getClass());
            System.out.println("============================================================================");
            start = Instant.now();
            calcAll(getFileLines(inputFilename));
            stop = Instant.now();

            System.out.println("============================================================================");
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

        if (filename == null) {
            return null;
        }

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

    protected String extract(String value, String regex) {
        var matcher = Pattern.compile(regex).matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new RuntimeException("Regex not found in value.");
    }
}
