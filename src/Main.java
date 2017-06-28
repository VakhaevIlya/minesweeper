import core.AI;
import core.Field;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Field field = new Field(30, 30, 100, null, true);
        final AI ai = new AI(field);
        while (!field.empty() && field.consistent()) {
            ai.advancedOpen();
            Main.printField(field, ai.getFlags(), 200, true);
        }
        Main.printField(field, ai.getFlags(), 0, false);
        System.out.println(field.consistent() ? "Success" : "Lose");
    }

    private static void printField(Field field, boolean[][] flags, int delay, boolean hide) throws InterruptedException {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < field.width; i++) {
            for (int j = 0; j < field.height; j++) {
                builder.append(' ');
                int numMines = field.aroundMines(i, j);
                if (field.isOpen(i, j)) {
                    if (field.isMine(i, j)) {
                        builder.append('X');
                    } else if (numMines == 0) {
                        builder.append(' ');
                    } else {
                        builder.append(numMines);
                    }
                } else {
                    if (!hide && field.isMine(i, j)) {
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
