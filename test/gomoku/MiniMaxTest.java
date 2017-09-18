package gomoku;

import org.junit.Test;

import static gomoku.Algorithm.LOSS_VALUE;
import static gomoku.Algorithm.WIN_VALUE;
import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MiniMaxTest {

    @Test
    public void chooseMove() {
        // O |   |
        // O | F |
        //(F)|   |
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(OPPONENT, 0, 1));

        Move move = new Move(FRIENDLY, 0, 2);


        assertEquals(LOSS_VALUE, new MiniMax(3).evaluateMove(new Move(FRIENDLY, 1, 2), board), 0.0);
        assertEquals(move, new MiniMax(3).chooseMove(FRIENDLY, board));
    }

    @Test
    public void evaluateMoveWin() {
        //  F | F |(F)
        //    |   |
        //    |   |
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1));
        Move move = new Move(FRIENDLY, 2, 0);

        assertEquals(WIN_VALUE, new MiniMax(3).evaluateMove(move, board), 0.0);
    }

    @Test
    public void evaluateMoveLoss() {
        //  O | O |
        //    |   |(F)
        //    |   |
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 1, 0));
        Move move = new Move(FRIENDLY, 2, 1);

        assertEquals(LOSS_VALUE, new MiniMax(3).evaluateMove(move, board), 0.0);
    }

    @Test
    public void evaluateMoveLoss2() {
        //  O | O |
        //  F | F | O
        // (F)| O | F
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(FRIENDLY, 2, 2))
                .withMove(new Move(OPPONENT, 1, 0));
        Move move = new Move(FRIENDLY, 2, 1);


        assertEquals(LOSS_VALUE, new MiniMax(3).evaluateMove(move, board), 0.0);
    }

}