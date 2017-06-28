package core;

import java.util.Random;

public class AI {

    private final Field field;
    private final boolean[][] flags;

    public AI(Field field) {
        this.field = field;
        this.flags = new boolean[field.width][field.height];
    }

    public void randomOpen() {
        final Random random = new Random();
        int x = random.nextInt(field.width);
        int y = random.nextInt(field.height);
        while (field.isOpen(x, y) || flags[x][y]) {
            x = random.nextInt(field.width);
            y = random.nextInt(field.height);
        }
        field.waveOpen(x, y, this::depthUpdateFlags);
    }

    public void advancedOpen() {
        for (int x = 0; x < field.width; x++) {
            for (int y = 0; y < field.height; y++) {
                if (!field.isOpen(x, y)) continue;
                final int numFlags = field.countAround(x, y, (Integer i, Integer j) -> flags[i][j]);
                final int numMines = field.aroundMines(x, y);
                if (numMines == numFlags) {
                    final Integer[] vector = {null, null};
                    field.visitAround(x, y, (Integer i, Integer j) -> {
                        if (!flags[i][j] && !field.isOpen(i, j)) {
                            vector[0] = i;
                            vector[1] = j;
                        }
                    });
                    if (vector[0] != null && vector[1] != null) {
                        field.waveOpen(vector[0], vector[1], this::depthUpdateFlags);
                        return;
                    }
                }
            }
        }
        randomOpen();
    }

    private void depthUpdateFlags(int x, int y) {
        updateFlags(x, y);
        field.visitAround(x, y, this::updateFlags);
    }

    private void updateFlags(int x, int y) {
        if (!field.isOpen(x, y)) return;
        final int numClosed = field.aroundClosed(x, y);
        final int numMines = field.aroundMines(x, y);
        if (numMines == 0) return;
        if (numClosed != numMines) return;
        field.visitAround(x, y, (Integer i, Integer j) -> {
            if (!field.isOpen(i, j)) flags[i][j] = true;
        });
    }

    public boolean[][] getFlags() {
        return flags;
    }
}
