import core.AI;
import core.Field;

public class AiTest {
    /**
     * 10x20:100 0,00%
     * 10x30:100 0,00%
     * 10x40:100 0,00%
     * 10x50:100 0,20%
     * 20x20:100 0,00%
     * 20x30:100 18,20%
     * 20x40:100 59,70%
     * 20x50:100 78,10%
     * 30x20:100 17,30%
     * 30x30:100 73,00%
     * 30x40:100 86,80%
     * 30x50:100 92,10%
     * 40x20:100 58,80%
     * 40x30:100 86,30%
     * 40x40:100 92,60%
     * 40x50:100 96,20%
     * 50x20:100 77,10%
     * 50x30:100 93,20%
     * 50x40:100 97,20%
     * 50x50:100 97,90%
     **/
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
