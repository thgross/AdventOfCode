package com.thgross.aoc;

import java.io.IOException;
import java.io.InputStream;

public abstract class Application {

    public abstract void run();

    protected InputStream getFileAsInputStream(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filename);
    }

    protected int[] removeElement(int[] arr, int removedIdx) {
        int[] dest = new int[arr.length - 1];

        System.arraycopy(arr, 0, dest, 0, removedIdx);
        System.arraycopy(arr, removedIdx + 1, dest, removedIdx, arr.length - 1 - removedIdx);

        return dest;
    }
}
