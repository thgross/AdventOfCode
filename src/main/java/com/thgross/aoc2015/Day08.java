package com.thgross.aoc2015;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 extends Application {

    String inputFilename = "aoc2015/input08.txt";

    public static void main(String[] args) {
        var app = (new Day08());
        app.run(app.inputFilename);
    }

    @SuppressWarnings("SameParameterValue")
    @Override
    protected void calcAll(List<String> lines) throws IOException {

        var charsCode = 0;
        var charsMemory = 0;
        var charsDoubleCode = 0;

        var pQuoteSimple = Pattern.compile("\"");
        var pQuotes = Pattern.compile("(^\"|\"$)");
        var pQuote = Pattern.compile("(\\\\\"|\\\\\\\\)");
        var pSlash = Pattern.compile("\\\\");
        var pHex = Pattern.compile("\\\\x[a-f0-9]{2}");

        for (String line : lines) {
            charsCode += line.length();

            var countQuotes = (int) pQuotes.matcher(line).results().count();
            var countQuote = (int) pQuote.matcher(line).results().count();
            var countHex = (int) pHex.matcher(line).results().count();
            // Diese Berechnung isrt nicht korrekt, da doppelt gefunden wird bei
            // "ubgxxcvnltzaucrzg\\xcez"
            var lineMemory = line.length() - countQuotes - countQuote - countHex * 3;

            var sMemory = line.toString();
            sMemory = pQuotes.matcher(sMemory).replaceAll("");
            sMemory = pQuote.matcher(sMemory).replaceAll("\"");
            sMemory = pHex.matcher(sMemory).replaceAll("§");
            var lineMemory2 = sMemory.length();

            var sDoubleQuote = line.toString();
            sDoubleQuote = pSlash.matcher(sDoubleQuote).replaceAll("\\\\\\\\");
            sDoubleQuote = pQuoteSimple.matcher(sDoubleQuote).replaceAll("\\\\\"");
            sDoubleQuote = "\"" + sDoubleQuote + "\"";
            var lineDoubleCode = sDoubleQuote.length();

/*
            System.out.printf("%2d:%-45s %2d (%2s):%-45s %2d:%-45s| Quotes: %2d, Quote: %2d, Hex: %2d\n",
                    line.length(), line,
                    lineMemory, sMemory.length(), sMemory,
                    lineDoubleCode, sDoubleQuote,
                    countQuotes, countQuote, countHex);
*/

            charsMemory += lineMemory2;
            charsDoubleCode += lineDoubleCode;
        }

        System.out.println("------------------------------------");
        System.out.printf("Part 1: charsCode: %d charsMemory: %d | charsCode - charsMemory: %d\n", charsCode, charsMemory, charsCode - charsMemory);
        System.out.printf("Part 2: charsDoubleCode: %d charsCode: %d | charsDoubleCode - charsCode: %d\n", charsDoubleCode, charsCode, charsDoubleCode - charsCode);
    }
}
