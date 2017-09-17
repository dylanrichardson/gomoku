package gomoku;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMaxTest {

    private static final String P1 = "A";
    private static final String P2 = "B";

    @Test
    public void chooseMove() {
        Board board = makeTestBoard();
        Move move = new Move(P2, 2, 2);

        assertEquals(move, new MiniMax(3).chooseMove(P2, board));
    }

    private Board makeTestBoard() {
        return new Board(3, 3)
                .withMove(new Move(P1, 0, 0))
                .withMove(new Move(P2, 1, 0))
                .withMove(new Move(P1, 1, 1));
        /* 1 | 2 |
             | 1 |
             |   |   */

    }

}