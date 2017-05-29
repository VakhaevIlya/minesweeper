package core;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class Field {

    public final int width;
    public final int height;
    private final int num_mines;
    private int open_fields;
    private int closed_fields;
    private final boolean[][] mines;
    private final boolean[][] opens;
    private final Long seed;
    private final boolean lazy;

    /**
     * Filed constructor
     *
     * @param width     is a width of the field, must be a positive
     * @param height    is a height of the field, must be a positive
     * @param num_mines is a number of mines for generating
     * @param seed      is a seed of the random, may be a null
     * @param lazy      if this parameter equals true then field generation will occur after first opening
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public Field(int width, int height, int num_mines, Long seed, boolean lazy) {
        this.width = width;
        this.height = height;
        this.num_mines = num_mines;
        this.closed_fields = this.width * this.height;
        this.open_fields = 0;
        if (this.width <= 0) throw new IllegalArgumentException();
        if (this.height <= 0) throw new IllegalArgumentException();
        if (this.closed_fields < this.num_mines) throw new IllegalArgumentException();
        this.mines = new boolean[this.width][this.height];
        this.opens = new boolean[this.width][this.height];
        this.seed = seed;
        this.lazy = lazy;
        if (!this.lazy) this.generate(this.seed);
    }

    /**
     * Randomly generating the mines on the filed.
     * Previously, this instance of core.Field must be empty and validated.
     *
     * @param seed is a seed of the random, may be a null
     */
    private void generate(Long seed) {
        final Random random = seed == null ? new Random() : new Random(seed);
        int i = 0;
        while (i < this.num_mines) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            if (!this.mines[x][y] && !this.opens[x][y]) {
                this.mines[x][y] = true;
                i++;
            }
        }
    }

    /**
     * @return true if a number of empty closed fields equals zero
     */
    public boolean empty() {
        return this.closed_fields - this.num_mines == 0;
    }

    /**
     * @return false if exist opened field with mine
     */
    public boolean consistent() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (this.is_open(i, j) && this.is_mine(i, j)) return false;
            }
        }
        return true;
    }

    /**
     * @param x        is a x component of the cell, must be in range 0..width-1
     * @param y        is a y component of the cell, must be in range 0..height-1
     * @param consumer is a visit function
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public void around(int x, int y, BiConsumer<Integer, Integer> consumer) {
        if (!this.is_valid(x, y)) throw new IllegalArgumentException();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (this.is_valid(i, j)) consumer.accept(i, j);
            }
        }
    }

    /**
     * @param x         is a x component of the cell, must be in range 0..width-1
     * @param y         is a y component of the cell, must be in range 0..height-1
     * @param condition is a condition of counting
     * @return a number of fields satisfying the condition
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public int around(int x, int y, BiPredicate<Integer, Integer> condition) {
        if (!this.is_valid(x, y)) throw new IllegalArgumentException();
        int counter = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (this.is_valid(i, j) && condition.test(i, j)) {
                    counter++;
                }
            }
        }
        return counter;

    }

    /**
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return a number of closed fields around the cell
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public int around_closed(int x, int y) {
        return this.around(x, y, (Integer i, Integer j) -> !this.opens[i][j]);
    }

    /**
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return a number of mines around the cell
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public int around_mines(int x, int y) {
        return this.around(x, y, (Integer i, Integer j) -> this.mines[i][j]);
    }

    /**
     * Opens the closed cell and returns false if the open was successful.
     *
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return true if this open was death for the master
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public boolean open(int x, int y) {
        if (!this.is_valid(x, y)) throw new IllegalArgumentException();
        if (this.opens[x][y]) throw new IllegalArgumentException();
        this.opens[x][y] = true;
        if (this.lazy && this.open_fields == 0) this.generate(this.seed);
        this.closed_fields--;
        this.open_fields++;
        return this.mines[x][y];
    }

    /**
     * Opens the closed cell and returns false if the open was successful.
     * If after opening the field which around there are no mines than these fields are opening.
     *
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return true if this open was death for the master
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public boolean wave_open(int x, int y) {
        final boolean result = this.open(x, y);
        if (this.around_mines(x, y) == 0) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (this.is_valid(i, j) && !this.is_open(i, j)) {
                        this.wave_open(i, j);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return true if this cell is open
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public boolean is_open(int x, int y) { //проверка на открытость ячеек
        if (!this.is_valid(x, y)) throw new IllegalArgumentException();
        return this.opens[x][y];
    }

    /**
     * @param x is a x component of the cell, must be in range 0..width-1
     * @param y is a y component of the cell, must be in range 0..height-1
     * @return true if this cell contains the mine
     * @throws IllegalArgumentException if parameters is not is_valid
     */
    public boolean is_mine(int x, int y) { //существует ли мина в этой ячейке
        if (!this.is_valid(x, y)) throw new IllegalArgumentException();
        return this.mines[x][y];
    }

    public boolean is_valid(int x, int y) {
        if (0 > x || x >= this.width) return false;
        if (0 > y || y >= this.height) return false;
        return true;
    }
}
