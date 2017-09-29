package gomoku;

import org.junit.Test;

import java.util.Collection;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgorithmTest {

    private static final Double TIME_LIMIT = 1.0 * 1000000000; // 1 sec

    private final Algorithm algorithm = new Algorithm();

    // evaluateMove

//    @Test
//    public void evaluateMoveWinEast() {
//        //  F | F |(F)
//        //    |   |
//        //    |   |
//        Board board = new Board(3, 3)
//                .withMove(new Move(FRIENDLY, 0, 0))
//                .withMove(new Move(FRIENDLY, 1, 0));
//        Move move = new Move(FRIENDLY, 2, 0);
//
//        assertEquals(WIN_VALUE, algorithm.evaluateMove(move, board, 3, TIME_LIMIT));
//    }
//
//    @Test
//    public void evaluateMoveLoss1() {
//        //  O | O |
//        //  F |   |(F)
//        //    |   |
//        Board board = new Board(3, 3)
//                .withMove(new Move(OPPONENT, 0, 0))
//                .withMove(new Move(FRIENDLY, 0, 1))
//                .withMove(new Move(OPPONENT, 1, 0));
//        Move move = new Move(FRIENDLY, 2, 1);
//
//        assertEquals(WIN_VALUE * -1, algorithm.evaluateMove(move, board, 3, TIME_LIMIT), 0);
//    }
//
//    @Test
//    public void evaluateMoveLoss2() {
//        // F |(F)|
//        // F | O |
//        // O |   |
//        Board board = new Board(3, 3)
//                .withMove(new Move(FRIENDLY, 0, 0))
//                .withMove(new Move(OPPONENT, 1, 1))
//                .withMove(new Move(FRIENDLY, 0, 1))
//                .withMove(new Move(OPPONENT, 0, 2));
//
//        Move move = new Move(FRIENDLY, 1, 0);
//
//        assertEquals(WIN_VALUE * -1, algorithm.evaluateMove(move, board, 3, TIME_LIMIT), 0);
//    }
//
//    @Test
//    public void evaluateMoveLoss3() {
//        //  O | O |
//        //  F | F | O
//        // (F)| O | F
//        Board board = new Board(3, 3)
//                .withMove(new Move(OPPONENT, 0, 0))
//                .withMove(new Move(FRIENDLY, 1, 1))
//                .withMove(new Move(OPPONENT, 2, 1))
//                .withMove(new Move(FRIENDLY, 0, 1))
//                .withMove(new Move(OPPONENT, 1, 2))
//                .withMove(new Move(FRIENDLY, 2, 2))
//                .withMove(new Move(OPPONENT, 1, 0));
//        Move move = new Move(FRIENDLY, 0, 2);
//
//
//        assertEquals(WIN_VALUE * -1, algorithm.evaluateMove(move, board, 3, TIME_LIMIT), 0);
//    }
//
//    @Test
//    public void evaluateMoveLoss4() {
//        //  F | F |
//        //  O | O | F
//        // (O)| F | O
//        Board board = new Board(3, 3)
//                .withMove(new Move(FRIENDLY, 0, 0))
//                .withMove(new Move(OPPONENT, 1, 1))
//                .withMove(new Move(FRIENDLY, 2, 1))
//                .withMove(new Move(OPPONENT, 0, 1))
//                .withMove(new Move(FRIENDLY, 1, 2))
//                .withMove(new Move(OPPONENT, 2, 2))
//                .withMove(new Move(FRIENDLY, 1, 0));
//        Move move = new Move(OPPONENT, 0, 2);
//
//
//        assertEquals(WIN_VALUE, algorithm.evaluateMove(move, board, 3, TIME_LIMIT));
//    }
//
//    @Test
//    public void evaluateMoveWin4x4() {
//        //  F | F | F |(F)
//        //    |   |   |
//        //    |   |   |
//        //    |   |   |
//        Board board = new Board(4, 4)
//                .withMove(new Move(FRIENDLY, 0, 0))
//                .withMove(new Move(FRIENDLY, 1, 0))
//                .withMove(new Move(FRIENDLY, 2, 0));
//        Move move = new Move(FRIENDLY, 3, 0);
//
//        assertEquals(WIN_VALUE, algorithm.evaluateMove(move, board, 4, TIME_LIMIT));
//    }
//
//    @Test
//    public void evaluateMoveLoss4x4() {
//        //  F | F | F |
//        //    |   |   |(O)
//        //    |   |   |
//        //    |   |   |
//        Board board = new Board(4, 4)
//                .withMove(new Move(FRIENDLY, 0, 0))
//                .withMove(new Move(FRIENDLY, 1, 0))
//                .withMove(new Move(FRIENDLY, 2, 0));
//        Move move = new Move(OPPONENT, 3, 1);
//
//        assertEquals(WIN_VALUE, algorithm.evaluateMove(move, board, 4, TIME_LIMIT * 5));
//    }
//
//    @Test
//    public void evaluateMoveDraw4x4() {
//        // (F)|   |   |
//        //    |   |   |
//        //    |   |   |
//        //    |   |   |
//        Board board = new Board(4, 4);
//        Move move = new Move(FRIENDLY, 0, 0);
//
//        assertEquals(DRAW_VALUE, algorithm.evaluateMove(move, board, 4, TIME_LIMIT));
//    }

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
//        Debug.print = true;
        Board board = new Board(15, 15);
        Move move = new Move(FRIENDLY, 0, 0);

        Long startTime = System.nanoTime();
        algorithm.evaluateMove(move, board, 5,0.0, 0.0, TIME_LIMIT);
        Long duration = System.nanoTime() - startTime;

        assertTrue("expected: " + TIME_LIMIT + " actual: " + duration,duration < TIME_LIMIT);
    }

    // getHeuristic

    @Test
    public void getHeuristic() {
        // TODO
        Board board = new Board(15, 15);
        Move move = new Move(FRIENDLY, 0, 0);
        algorithm.getHeuristic(board, move);
    }

    // getPossibleMoves

    @Test
    public void getPossibleMoves() {
        // TODO
        Board board = new Board(15, 15);
        algorithm.getPossibleMoves(FRIENDLY, board);
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

    // defensive strategy

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
    public void chooseMoveBlock2ndWinEast() {
        //   |   |   |   |
        //(F)| O | O |   |
        //   |   |   |   |
        Board board = new Board(5, 3)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1));

        Move move = new Move(FRIENDLY, 3, 1);

        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 4, TIME_LIMIT));
    }

    // intersection of 2 2-away wins
    // opponent move would produce 2 ways to win

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

//    @Test
//    public void chooseMoveBlockCombo1() {
//        //   |   |   |   |
//        // O | O | O |(F)|
//        //   |   |   |(F)|
//        //   |   |   | O |
//        //   |   |   | O |
//        //   |   |   | O |
//        Board board = new Board(4, 6)
//                .withMove(new Move(OPPONENT, 0, 1))
//                .withMove(new Move(OPPONENT, 1, 1))
//                .withMove(new Move(OPPONENT, 2, 1))
//                .withMove(new Move(OPPONENT, 3, 3))
//                .withMove(new Move(OPPONENT, 3, 4))
//                .withMove(new Move(OPPONENT, 3, 5));
//
//        Collection<Move> possibleMoves = asList(
//                new Move(FRIENDLY, 3, 1),
//                new Move(FRIENDLY, 3, 2));
//
//        Move move = algorithm.chooseMove(FRIENDLY, board, 5, TIME_LIMIT);
//        assertTrue("move: " + move, possibleMoves.contains(move));
//    }
//
//    @Test
//    public void chooseMoveBlockCombo3() {
//        //   |   |   |   |
//        //   |   |   | O |
//        //   |   |   |(F)|
//        //   |   |   | O |
//        //   |   |   | O |
//        Board board = new Board(4, 5)
////                .withMove(new Move(OPPONENT, 0, 2))
////                .withMove(new Move(OPPONENT, 1, 2))
////                .withMove(new Move(OPPONENT, 2, 2))
//                .withMove(new Move(OPPONENT, 3, 1))
//                .withMove(new Move(OPPONENT, 3, 3))
//                .withMove(new Move(OPPONENT, 3, 4));
//
//        Move move = new Move(FRIENDLY, 3, 2);
//        assertEquals(move, algorithm.chooseMove(FRIENDLY, board, 5, TIME_LIMIT));
//    }

    // TODO combos of strategies

}