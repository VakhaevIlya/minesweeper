import core.AI;
import core.Field;

public class AiTest {

    public static void main(String[] args) {

        final int numCycles = 1000;
        final int numMines = 100;
        for (int i = 1; i < 6; i++) {
            for (int j = 2; j < 6; j++) {
                final int width = i * 10;
                final int height = j * 10;
                final float successRate = test(width, height, numMines, numCycles) * 100;
                System.out.println(String.format("%dx%d:%d %.2f%%", width, height, numMines, successRate));
            }
        }
    }

    private static float test(int width, int height, int numMines, int numCycles) {
        int successCounter = 0;
        for (int i = 0; i < numCycles; i++) {
            if (test(width, height, numMines)) {
                successCounter++;
            }
        }
        return successCounter / (float) numCycles;
    }

    private static boolean test(int width, int height, int numMines) {
        final Field field = new Field(width, height, numMines, null, true);
        final AI ai = new AI(field);
        while (!field.empty() && field.consistent()) {
            ai.advancedOpen();
        }
        return field.consistent();
    }
}
