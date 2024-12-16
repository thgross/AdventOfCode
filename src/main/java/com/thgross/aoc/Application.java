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
}
