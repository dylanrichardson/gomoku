package gomoku;

import org.junit.Test;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgorithmTest {

    private static final Double TIME_LIMIT = 1.0 * 1000000000; // 1 sec

    private final Algorithm algorithm = new Algorithm();

    // time limit

    @Test
    public void chooseMoveInTimeLimit() {
        Board board = new Board(15, 15)
                .withMove(new Move(OPPONENT, 0 , 0));

        Long startTime = System.nanoTime();
        algorithm.chooseMove(FRIENDLY, board, 5, TIME_LIMIT);
        Long duration = System.nanoTime() - startTime;

        assertTrue("expected: " + TIME_LIMIT + " actual: " + duration,duration < TIME_LIMIT);
    }

    @Test
    public void evaluateMoveInTimeLimit() {
        Board board = new Board(15, 15);
        Move move = new Move(FRIENDLY, 0, 0);

        Long startTime = System.nanoTime();
        algorithm.evaluateMove(move, board, 5,0.0, 0.0, TIME_LIMIT);
        Long duration = System.nanoTime() - startTime;

        assertTrue("expected: " + TIME_LIMIT + " actual: " + duration,duration < TIME_LIMIT);
    }

    @Test
    public void chooseMove() {
        // O |   |
        // O | F |
        //(O)|   |
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(OPPONENT, 0, 1));

        Move move = new Move(OPPONENT, 0, 2);
        assertEquals(move, algorithm.chooseMove(OPPONENT, board, 3, TIME_LIMIT));
    }

    // offensive strategy

    @Test
    public void chooseMoveOpponentWinNorth() {
        // O |   |
        // O | F |
        //(O)|   |
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(OPPONENT, 0, 1));

        Move move = new Move(OPPONENT, 0, 2);
        assertEquals(move, algorithm.chooseMove(OPPONENT, board, 3, TIME_LIMIT));
    }

    @Test
    public void chooseMoveWinOverBlock() {
        //    |(F)|   |
        //  O | F |   |
        //  O | F |   |
        //  O | F |   |
        Board board = new Board(4, 4)
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2))
                .withMove(new Move(OPPONENT, 0, 3))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 1, 2))
                .withMove(new Move(FRIENDLY, 1, 3));

        Move move = new Move(FRIENDLY, 1, 0);
        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 4, TIME_LIMIT));
    }

    @Test
    public void chooseMoveBlockNorth() {
        // O |   |
        // O | F |
        //(F)|   |
        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(OPPONENT, 0, 1));

        Move move = new Move(FRIENDLY, 0, 2);
        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 3, TIME_LIMIT));
    }

    @Test
    public void chooseMoveBlockSouthWest() {
        // F |   |(F)
        // F | O |
        // O |   |
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2));

        Move move = new Move(FRIENDLY, 2, 0);

        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 3, TIME_LIMIT));
    }

    @Test
    public void chooseMoveBlockCombo() {
        //   |   |   |   |
        // O | O | O |(F)|
        //   |   |   | O |
        //   |   |   | O |
        //   |   |   | O |
        Board board = new Board(4, 5)
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 3, 4));

        Move move = new Move(FRIENDLY, 3, 1);
        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 5, TIME_LIMIT));
    }

}