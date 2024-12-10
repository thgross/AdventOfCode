package com.thgross;

import com.thgross.aoc.Application;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AOC2024_09 extends Application {
    public static void main(String[] args) {
        (new AOC2024_09()).run();
    }

    protected static class FBlock {
        int id;
        int count;

        public FBlock(int id, int count) {
            this.id = id;
            this.count = count;
        }
    }

    static class Pdata {
        List<FBlock> map = new ArrayList<>();
        List<FBlock> map2 = new ArrayList<>();
        long checksum = 0;
        long checksum2 = 0;
    }

    private final Pdata lc = new Pdata();
    private boolean enableDump = false;

    @Override
    public void run() {
        try {
            Instant start = Instant.now();
            calcAll("input09.txt");
            Instant stop = Instant.now();

            System.out.printf("Runtime: %s\n", Duration.between(start, stop));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void calcAll(String inputFile) throws IOException {

        var content = getFileContent(inputFile);

        // Parse
        boolean isData = true;
        int id = 0;
        for (int i = 0; i < content.length(); i++) {
            var count = Integer.parseInt(content.substring(i, i + 1));
            if (isData) {
                if (count > 0) {
                    lc.map.add(new FBlock(id, count));
                    lc.map2.add(new FBlock(id, count));
                    id++;
                }
            } else {
                if (count > 0) {
                    lc.map.add(new FBlock(-1, count));
                    lc.map2.add(new FBlock(-1, count));
                }
            }
            isData = !isData;
        }

        enableDump = false;
//        enableDump = true;

        dump(lc.map);
        rearrange(lc.map);
        lc.checksum = checksum(lc.map);

        dump(lc.map2);
        rearrange2(lc.map2);
        lc.checksum2 = checksum(lc.map2);

        System.out.printf("checksum: %d\n", lc.checksum);
        System.out.printf("checksum2: %d\n", lc.checksum2);
    }

    protected long checksum(List<FBlock> map) {

        long checksum = 0;
        int pos = 0;

        for (FBlock fBlock : map) {
            for (int bid = 0; bid < fBlock.count; bid++) {
                if (fBlock.id != -1) {
                    checksum += (long) pos * fBlock.id;
                }
                pos++;
            }
        }

        return checksum;
    }

    protected void dump(List<FBlock> map) {
        if (!enableDump) {
            return;
        }
        for (FBlock fBlock : map) {
            if (fBlock.id == -1) {
                System.out.printf(".".repeat(fBlock.count));
            } else {
                System.out.printf(String.valueOf(fBlock.id).repeat(fBlock.count));
            }
        }
        System.out.println();
    }

    protected void rearrange(List<FBlock> map) {
        do {
            var fsi = getFreespace(map);
            if (fsi != null) {
                var bli = getLastFBlock(map);
                if (fsi < bli) {
                    moveBlockToFreespace(map, bli, fsi);
                    dump(map);
                } else {
                    break;
                }
            } else {
                break;
            }
        } while (true);
    }

    protected void rearrange2(List<FBlock> map) {

        for (int bli = map.size() - 1; bli >= 0; bli--) {
            var fBlock = map.get(bli);
            if (fBlock.id != -1) {  // ist Block
                // Lücke finden
                for (int fsi = 0; fsi < bli; fsi++) {
                    var fsBlock = map.get(fsi);
                    if (fsBlock.id == -1 && fsBlock.count >= fBlock.count) {  // ist Lücke
                        var offset = moveBlockToFreespace(map, bli, fsi);
                        // Der Loop muss erweitert werden, weil wir Elemente hinzugefügt haben!
                        bli += offset;
                        dump(map);
                        break;
                    }
                }
            }
        }
    }

    protected int moveBlockToFreespace(List<FBlock> map, int bli, int fsi) {
        var offset = 0;
        var bl = map.get(bli);
        var fs = map.get(fsi);
        if (bl.count > fs.count) {
            fs.id = bl.id;
            bl.count -= fs.count;
            map.add(bli + 1, new FBlock(-1, fs.count));
            offset = 1; // Map ist um 1 Element größer als vorher
        } else if (bl.count < fs.count) {
            map.add(fsi, map.remove(bli));
            fs.count -= bl.count;
            map.add(bli + 1, new FBlock(-1, bl.count));
            offset = 1; // Map ist um 1 Element größer als vorher
        } else {
            map.add(fsi, map.remove(bli));
            map.add(bli, map.remove(fsi + 1));
        }

        return offset;
    }

    protected Integer getFreespace(List<FBlock> map) {
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).id == -1) {
                return i;
            }

        }

        return null;
    }

    protected Integer getLastFBlock(List<FBlock> map) {
        for (int i = map.size() - 1; i >= 0; i--) {
            if (map.get(i).id != -1) {
                return i;
            }
        }

        return null;
    }
}
