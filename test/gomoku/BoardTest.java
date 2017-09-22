package gomoku;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void withMove3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        assertEquals(FRIENDLY, board.getStoneInCell(0, 0));
    }
    @Test
    public void withMoveNoMutation3x3() {
        Board board1 = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        board1.withMove(new Move(OPPONENT, 0, 0));
        assertEquals(FRIENDLY, board1.getStoneInCell(0, 0));
    }

    @Test
    public void withMoveOnMove3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 0, 0));
        assertEquals(OPPONENT, board.getStoneInCell(0, 0));
    }

    @Test
    public void getOpenCells3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        Collection<Pair<Integer, Integer>> openCells = Arrays.asList(
                new Pair<>(0,1),
                new Pair<>(0,2),
                new Pair<>(1,0),
                new Pair<>(1,1),
                new Pair<>(1,2),
                new Pair<>(2,0),
                new Pair<>(2,1),
                new Pair<>(2,2));
        assertEquals(openCells, board.getOpenCells());
    }

    @Test
    public void isTerminalVerticalWin3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalHorizontalWin3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalDiagonalWin13x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 2, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalDiagonalWin23x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalDraw3x3() {
        //  F | O | F
        //  F | O | F
        //  O | F | O
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2))
                .withMove(new Move(OPPONENT, 1, 0))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(FRIENDLY, 1, 2))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 2, 1))
                .withMove(new Move(OPPONENT, 2, 2));
        assertTrue(board.isTerminal(3));
        assertNull(board.getWinner(3));
    }

    @Test
    public void isTerminalDraw4x4() {
        //  F | F | F | O
        //  F | O | F | O
        //  O | F | O | F
        //  F | O | F | O
        Board board = new Board(4, 4)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2))
                .withMove(new Move(FRIENDLY, 0, 3))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(FRIENDLY, 1, 2))
                .withMove(new Move(OPPONENT, 1, 3))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 2, 1))
                .withMove(new Move(OPPONENT, 2, 2))
                .withMove(new Move(FRIENDLY, 2, 3))
                .withMove(new Move(OPPONENT, 3, 0))
                .withMove(new Move(OPPONENT, 3, 1))
                .withMove(new Move(FRIENDLY, 3, 2))
                .withMove(new Move(OPPONENT, 3, 3));
        assertTrue(board.isTerminal(4));
        assertNull(board.getWinner(4));
    }

    @Test
    public void isTerminalFalse3x3() {
        assertNull(new Board(3,3).getWinner(3));
    }

    @Test
    public void getWinnerFriendly3x3() {

        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void getWinnerOpponent3x3() {

        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2));
        assertEquals(OPPONENT, board.getWinner(3));
    }

    @Test
    public void getWinnerDraw3x3() {

        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(null, board.getWinner(3));
    }

    @Test
    public void isValidMoveTrue3x3() {
        Board board = new Board(3, 3);
        Move move = new Move(FRIENDLY, 0, 0);
        assertTrue(board.isValidMove(move));
    }

    @Test
    public void isValidMoveFalse3x3() {
        Board board = new Board(3, 3);
        Move move = new Move(FRIENDLY, 3, 3);
        assertFalse(board.isValidMove(move));
    }

    @Test
    public void isValidMoveFalseNeg3x3() {
        Board board = new Board(3, 3);
        Move move = new Move(FRIENDLY, -1, -1);
        assertFalse(board.isValidMove(move));
    }

    @Test
    public void print3x3() {
        String boardString = " F | F | O \n"
                           + " O |   |   \n"
                           + "   |   |   \n";
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 2, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(OPPONENT, 0, 1));
        assertEquals(boardString, board.toString());
    }

    // TODO add tests for isTerminalMove/isTerminal on 4x4 3 win length

}