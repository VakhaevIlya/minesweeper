package core;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class Field {

    public final int width;
    public final int height;
    private final int numMines;
    private int openFields;
    private int closedFields;
    private final boolean[][] mines;
    private final boolean[][] opens;
    private final Long seed;
    private final boolean lazy;

   
    public Field(int width, int height, int numMines, Long seed, boolean lazy) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        this.closedFields = this.width * this.height;
        this.openFields = 0;
        if (this.width <= 0) throw new IllegalArgumentException();
        if (this.height <= 0) throw new IllegalArgumentException();
        if (this.closedFields < this.numMines) throw new IllegalArgumentException();
        this.mines = new boolean[this.width][this.height];
        this.opens = new boolean[this.width][this.height];
        this.seed = seed;
        this.lazy = lazy;
        if (!this.lazy) this.generate(this.seed);
    }

    
    private void generate(Long seed) {
        final Random random = seed == null ? new Random() : new Random(seed);
        int i = 0;
        while (i < this.numMines) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            if (!this.mines[x][y] && !this.opens[x][y]) {
                this.mines[x][y] = true;
                i++;
            }
        }
    }

   
    public boolean empty() {
        return this.closedFields - this.numMines == 0;
    }

        public boolean consistent() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (this.isOpen(i, j) && this.isMine(i, j)) return false;
            }
        }
        return true;
    }

   
    public void visitAround(int x, int y, BiConsumer<Integer, Integer> consumer) {
        if (!this.isValid(x, y)) throw new IllegalArgumentException();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) continue;
                if (!this.isValid(i, j)) continue;
                consumer.accept(i, j);
            }
        }
    }

   
    public int countAround(int x, int y, BiPredicate<Integer, Integer> condition) {
        if (!this.isValid(x, y)) throw new IllegalArgumentException();
        final int[] counter = {0};
        visitAround(x, y, (Integer i, Integer j) -> {
            if (condition.test(i, j)) counter[0]++;
        });
        return counter[0];

    }


    public int aroundClosed(int x, int y) {
        return this.countAround(x, y, (Integer i, Integer j) -> !this.opens[i][j]);
    }

   
    public int aroundMines(int x, int y) {
        return this.countAround(x, y, (Integer i, Integer j) -> this.mines[i][j]);
    }

   
    public boolean open(int x, int y) {
        if (!this.isValid(x, y)) throw new IllegalArgumentException();
        if (this.opens[x][y]) throw new IllegalArgumentException();
        this.opens[x][y] = true;
        if (this.lazy && this.openFields == 0) this.generate(this.seed);
        this.closedFields--;
        this.openFields++;
        return this.mines[x][y];
    }

 
    public boolean waveOpen(int x, int y, BiConsumer<Integer, Integer> consumer) {
        final boolean result = this.open(x, y);
        if (consumer != null) consumer.accept(x, y);
        if (this.aroundMines(x, y) == 0) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (this.isValid(i, j) && !this.isOpen(i, j)) {
                        this.waveOpen(i, j, consumer);
                    }
                }
            }
        }
        return result;
    }

    
    public boolean waveOpen(int x, int y) {
        return waveOpen(x, y, null);
    }

 
    public boolean isOpen(int x, int y) {
        if (!this.isValid(x, y)) throw new IllegalArgumentException();
        return this.opens[x][y];
    }

  
    public boolean isMine(int x, int y) {
        if (!this.isValid(x, y)) throw new IllegalArgumentException();
        return this.mines[x][y];
    }

    public boolean isValid(int x, int y) {
        if (0 > x || x >= this.width) return false;
        if (0 > y || y >= this.height) return false;
        return true;
    }
}
