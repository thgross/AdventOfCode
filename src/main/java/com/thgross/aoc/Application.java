package com.thgross.aoc;

import java.io.IOException;
import java.io.InputStream;

public abstract class Application {

    public abstract void run();

    protected InputStream getFileAsInputStream(String filename) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filename);
    }
}
