import core.Field;
import org.junit.Test;

public class FieldTest {
    @Test
    public void empty() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert !field.empty();
        field.waveOpen(2, 2);
        assert !field.empty();
        field.waveOpen(9, 9);
        assert !field.empty();
        field.waveOpen(0, 0);
        assert !field.empty();
        field.waveOpen(0, 6);
        assert !field.empty();
        field.waveOpen(6, 8);
        assert !field.empty();
        field.waveOpen(6, 0);
        assert !field.empty();
        field.waveOpen(9, 0);
        assert !field.empty();
        field.waveOpen(6, 2);
        assert !field.empty();
        field.waveOpen(7, 2);
        assert !field.empty();
        field.waveOpen(7, 3);
        assert !field.empty();
        field.waveOpen(8, 3);
        assert !field.empty();
        field.waveOpen(8, 4);
        assert !field.empty();
        field.waveOpen(9, 2);
        assert !field.empty();
        field.waveOpen(9, 3);
        assert field.empty();
    }

    @Test
    public void consistent() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert field.consistent();
        field.waveOpen(2, 2);
        assert field.consistent();
        field.waveOpen(9, 9);
        assert field.consistent();
        field.waveOpen(0, 0);
        assert field.consistent();
        field.waveOpen(0, 6);
        assert field.consistent();
        field.waveOpen(6, 8);
        assert field.consistent();
        field.waveOpen(6, 0);
        assert field.consistent();
        field.waveOpen(9, 0);
        assert field.consistent();
        field.waveOpen(6, 2);
        assert field.consistent();
        field.waveOpen(0, 1);
        assert !field.consistent();
    }

    @Test
    public void visitAround() {
        this.aroundClosed();
        this.aroundMines();
    }

    @Test
    public void aroundClosed() {
        final Field field = new Field(10, 10, 10, 3L, false);
        field.waveOpen(2, 2);
        field.waveOpen(9, 9);
        field.waveOpen(0, 0);
        field.waveOpen(0, 6);
        field.waveOpen(6, 8);
        assert field.aroundClosed(0, 0) == 1;
        assert field.aroundClosed(6, 3) == 5;
        assert field.aroundClosed(6, 4) == 2;
        assert field.aroundClosed(7, 5) == 2;
        assert field.aroundClosed(9, 0) == 3;
        assert field.aroundClosed(8, 1) == 8;
    }

    @Test
    public void aroundMines() {
        final Field field = new Field(10, 10, 10, 3L, false);
        field.waveOpen(2, 2);
        field.waveOpen(9, 9);
        field.waveOpen(0, 0);
        field.waveOpen(0, 6);
        field.waveOpen(6, 8);
        assert field.aroundMines(0, 0) == 1;
        assert field.aroundMines(0, 1) == 0;
        assert field.aroundMines(2, 1) == 0;
        assert field.aroundMines(3, 1) == 1;
        assert field.aroundMines(4, 1) == 2;
        assert field.aroundMines(8, 3) == 3;
    }

    @Test
    public void open() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert !field.isOpen(2, 2);
        assert !field.isOpen(9, 9);
        assert !field.isOpen(0, 0);
        assert !field.isOpen(0, 6);
        assert !field.isOpen(6, 8);
        field.open(2, 2);
        field.open(9, 9);
        field.open(0, 0);
        field.open(0, 6);
        field.open(6, 8);
        assert field.isOpen(2, 2);
        assert field.isOpen(9, 9);
        assert field.isOpen(0, 0);
        assert field.isOpen(0, 6);
        assert field.isOpen(6, 8);
    }

    @Test
    public void waveOpen() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert !field.isOpen(2, 2);
        assert !field.isOpen(9, 9);
        assert !field.isOpen(0, 0);
        assert !field.isOpen(0, 6);
        assert !field.isOpen(6, 8);
        for (int i = 2; i < 10; i++) assert !field.isOpen(0, i);
        for (int i = 0; i < 10; i++) assert !field.isOpen(2, i);
        for (int i = 0; i < 10; i++) assert !field.isOpen(3, i);
        field.waveOpen(2, 2);
        field.waveOpen(9, 9);
        field.waveOpen(0, 0);
        field.waveOpen(0, 6);
        field.waveOpen(6, 8);
        assert field.isOpen(2, 2);
        assert field.isOpen(9, 9);
        assert field.isOpen(0, 0);
        assert field.isOpen(0, 6);
        assert field.isOpen(6, 8);
        for (int i = 2; i < 10; i++) assert field.isOpen(0, i);
        for (int i = 0; i < 10; i++) assert field.isOpen(2, i);
        for (int i = 0; i < 10; i++) assert field.isOpen(3, i);
        assert field.consistent();
    }

    @Test
    public void isOpen() {
        this.open();
        this.waveOpen();
    }

    @Test
    public void isMine() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert !field.isMine(2, 2);
        assert !field.isMine(9, 9);
        assert !field.isMine(0, 0);
        assert !field.isMine(0, 6);
        assert !field.isMine(6, 8);
        assert field.isMine(0, 1);
        assert field.isMine(4, 0);
        assert field.isMine(5, 2);
        assert field.isMine(8, 2);
        assert field.isMine(6, 7);
    }

    @Test
    public void isValid() {
        final Field field = new Field(10, 10, 10, 3L, false);
        assert field.isValid(0, 0);
        assert field.isValid(0, 2);
        assert field.isValid(3, 9);
        assert field.isValid(9, 9);
        assert field.isValid(0, 9);
        assert field.isValid(9, 0);
        assert !field.isValid(-1, -1);
        assert !field.isValid(0, -1);
        assert !field.isValid(-1, 0);
        assert !field.isValid(3, -20);
        assert !field.isValid(10, 9);
        assert !field.isValid(9, 10);
        assert !field.isValid(21, 0);
    }
}
