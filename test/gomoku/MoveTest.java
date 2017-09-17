package gomoku;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {
    @Test
    public void toStringIntConstructor() {
        Move move = new Move("name", 1, 0);

        assertEquals("name b 0", move.toString());
    }

    @Test
    public void toStringCharConstructor() {
        Move move = new Move("name", 'B', 0);

        assertEquals("name b 0", move.toString());
    }

}