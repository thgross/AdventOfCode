package com.thgross.aoc;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Winframe extends Frame {
    public Graphics2D g;

    public Winframe(String title, int w, int h) {
        setTitle(title);
        setSize(w, h);
        addWindowListener(new WinframeListener());

        setVisible(true);

        g = (Graphics2D) getGraphics();
    }

    static class WinframeListener extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            event.getWindow().dispose();
            System.exit(0);
        }

        @Override
        public void windowStateChanged(WindowEvent e) {
            super.windowStateChanged(e);
            System.out.println(e.getWindow().getSize());
        }
    }
}
