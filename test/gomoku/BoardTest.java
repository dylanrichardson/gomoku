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
    public void withMove() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        assertEquals(FRIENDLY, board.getStoneInCell(0, 0));
    }
    @Test
    public void withMoveNoMutation() {
        Board board1 = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        board1.withMove(new Move(OPPONENT, 0, 0));
        assertEquals(FRIENDLY, board1.getStoneInCell(0, 0));
    }

    @Test
    public void withMoveOnMove() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 0, 0));
        assertEquals(OPPONENT, board.getStoneInCell(0, 0));
    }

    @Test
    public void getOpenCells() {
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
    public void isTerminalVerticalWin() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalHorizontalWin() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalDiagonalWin() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 2, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void isTerminalDraw() {
        // O | O | X
        // X | X | O
        // O | O | X
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
    public void isTerminalFalse() {
        assertNull(new Board().getWinner(3));
    }

    @Test
    public void getWinnerFriendly() {

        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(FRIENDLY, board.getWinner(3));
    }

    @Test
    public void getWinnerOpponent() {

        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2));
        assertEquals(OPPONENT, board.getWinner(3));
    }

    @Test
    public void getWinnerDraw() {

        Board board = new Board(3, 3)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2));
        assertEquals(null, board.getWinner(3));
    }

    @Test
    public void isValidMoveTrue() {
        Board board = new Board(3, 3);
        Move move = new Move(FRIENDLY, 0, 0);
        assertTrue(board.isValidMove(move));
    }

    @Test
    public void isValidMoveFalse() {
        Board board = new Board(3, 3);
        Move move = new Move(FRIENDLY, 3, 3);
        assertFalse(board.isValidMove(move));
    }

}