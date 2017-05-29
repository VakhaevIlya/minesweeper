package core;

import java.util.Random;

public class AI {

    public static boolean[][] random_open(Field field) {
        final boolean[][] flags = AI.flags(field);
        final Random random = new Random();
        int x = random.nextInt(field.width);
        int y = random.nextInt(field.height);
        while (field.is_open(x, y) || flags[x][y]) {
            x = random.nextInt(field.width);
            y = random.nextInt(field.height);
        }
        field.wave_open(x, y);
        return flags;
    }

    public static boolean[][] advanced_open(Field field) {
        final boolean[][] flags = AI.flags(field);
        for (int x = 0; x < field.width; x++) {
            for (int y = 0; y < field.height; y++) {
                if (!field.is_open(x, y)) continue;
                final int num_flags = field.around(x, y, (Integer i, Integer j) -> flags[i][j]);
                final int num_mines = field.around_mines(x, y);
                if (num_mines == num_flags) {
                    final Integer[] vector = {null, null};
                    field.around(x, y, (Integer i, Integer j) -> {
                        if (!flags[i][j] && !field.is_open(i, j)) {
                            vector[0] = i;
                            vector[1] = j;
                        }
                    });
                    if (vector[0] != null && vector[1] != null) {
                        field.wave_open(vector[0], vector[1]);
                        return flags;
                    }
                }
            }
        }
        System.out.println("random open");
        return AI.random_open(field);
    }

    public static boolean[][] flags(Field field) {
        final boolean[][] flags = new boolean[field.width][field.height];
        for (int x = 0; x < field.width; x++) {
            for (int y = 0; y < field.height; y++) {
                if (!field.is_open(x, y)) continue;
                final int num_closed = field.around_closed(x, y);
                final int num_mines = field.around_mines(x, y);
                if (num_mines == 0) continue;
                if (num_closed != num_mines) continue;
                field.around(x, y, (Integer i, Integer j) -> {
                    if (!field.is_open(i, j)) flags[i][j] = true;
                });
            }
        }
        return flags;
    }

}
