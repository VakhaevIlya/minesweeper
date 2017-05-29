import core.AI;
import core.Field;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Field field = new Field(30, 30, 100, null, true);
        boolean[][] flags = new boolean[field.width][field.height];
        while (!field.empty() && field.consistent()) {
            flags = AI.advanced_open(field);
            Main.print_field(field, flags, 200, true);
        }
        Main.print_field(field, flags, 0, false);
        System.out.println(field.consistent() ? "Success" : "Lose");
    }

    private static void print_field(Field field, boolean[][] flags, int delay, boolean hide) throws InterruptedException {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < field.width; i++) {
            for (int j = 0; j < field.height; j++) {
                builder.append(' ');
                int num_mines = field.around_mines(i, j);
                if (field.is_open(i, j)) {
                    if (field.is_mine(i, j)) {
                        builder.append('X');
                    } else if (num_mines == 0) {
                        builder.append(' ');
                    } else {
                        builder.append(num_mines);
                    }
                } else {
                    if (!hide && field.is_mine(i, j)) {
                        builder.append('*');
                    } else if (flags[i][j]) {
                        builder.append('f');
                    } else {
                        builder.append('_');
                    }
                }
            }
            builder.append(" \n");
        }
        System.out.print(builder);
        Thread.sleep(delay);
        System.out.println("==============================================");
    }
}
