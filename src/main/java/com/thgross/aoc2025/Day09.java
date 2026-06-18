package com.thgross.aoc2025;

import com.thgross.aoc.Application;
import com.thgross.aoc.Point3D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 extends Application {

    String inputFilename = "aoc2025/input09.txt";

    static void main() {
        var app = (new Day09());
        app.run(app.inputFilename);
    }

    @Override
    protected void calcAll(List<String> lines) throws IOException {

        long part1LargestRectSize = 0;
        long part2LargestRectSize = 0;
        long tmpRectSize = 0;
        Point part1LargestRectP1 = null;
        Point part1LargestRectP2 = null;

        List<Point> points = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }

        BufferedImage bi = createMap(points);

        for (int i1 = 0; i1 < points.size(); i1++) {
            for (int i2 = i1 + 1; i2 < points.size(); i2++) {
                Point p1 = points.get(i1);
                Point p2 = points.get(i2);
                tmpRectSize = Math.abs((1L + p1.x - p2.x) * (1L + p1.y - p2.y));
                if (tmpRectSize > part1LargestRectSize) {
                    part1LargestRectSize = tmpRectSize;
                    part1LargestRectP1 = p1;
                    part1LargestRectP2 = p2;
                }
            }
        }

        drawRect(bi, part1LargestRectP1, part1LargestRectP2);

        saveImage(bi);

        System.out.println("------------------------------------");
        System.out.printf("Part 1: Largest Rectangle: %d\n", part1LargestRectSize);
    }

    private void drawRect(BufferedImage bi, Point p1, Point p2) {
        var g2d = bi.getGraphics();

        g2d.setColor(Color.MAGENTA);
        g2d.drawRect(Math.min(p1.x, p2.x)/100, Math.min(p1.y, p2.y)/100, Math.abs(p1.x - p2.x)/100, Math.abs(p1.y - p2.y)/100);
    }

    private void saveImage(BufferedImage bi) {
        try {
            ImageIO.write(bi, "PNG", new File("_tmp/aoc2025-09-image.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public BufferedImage createMap(List<Point> points) {
        var bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        var g2d = bi.getGraphics();
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < points.size(); i++) {
            int iPrev = i - 1;
            if (iPrev < 0) {
                iPrev = points.size() - 1;
            }
            Point p1 = points.get(i);
            Point p2 = points.get(iPrev);

            // Draw Line between p1 and p2 on g2d
            g2d.drawLine(p1.x / 100, p1.y / 100, p2.x / 100, p2.y / 100);
        }

        return bi;
    }
}
