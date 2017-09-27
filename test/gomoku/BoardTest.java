package gomoku;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static gomoku.Algorithm.DRAW_VALUE;
import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static org.junit.Assert.*;

public class BoardTest {

    // withMove

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

    // getOpenCells

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

    // isValidMove

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

    // toString

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

    // willBeTerminalCell

    @Test
    public void willBeTerminalCellWin4West() {
        // F | F | F | F |(F)| ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0));

        assertTrue(board.willBeTerminalCell(4, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin4East() {
        //(F)| F | F | F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeTerminalCell(0, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin3West1East() {
        // F | F | F |(F)| F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeTerminalCell(3, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin2West2East() {
        // F | F |(F)| F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeTerminalCell(2, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin4North() {
        // F |   | ...
        // F |   | ...
        // F |   | ...
        // F |   | ...
        //(F)|   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2))
                .withMove(new Move(FRIENDLY, 0, 3));

        assertTrue(board.willBeTerminalCell(0, 4, 5));
    }

    @Test
    public void willBeTerminalCellWin4South() {
        //(F)|   | ...
        // F |   | ...
        // F |   | ...
        // F |   | ...
        // F |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 2))
                .withMove(new Move(FRIENDLY, 0, 3))
                .withMove(new Move(FRIENDLY, 0, 4));

        assertTrue(board.willBeTerminalCell(0, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin2North2South() {
        // F |   | ...
        // F |   | ...
        //(F)|   | ...
        // F |   | ...
        // F |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 0, 1))
                .withMove(new Move(FRIENDLY, 0, 3))
                .withMove(new Move(FRIENDLY, 0, 4));

        assertTrue(board.willBeTerminalCell(0, 2, 5));
    }

    @Test
    public void willBeTerminalCellWin4NorthEast() {
        //   |   |   |   | F | ...
        //   |   |   | F |   |   ...
        //   |   | F |   |   |   ...
        //   | F |   |   |   |   ...
        //(F)|   |   |   |   |   ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 4, 0))
                .withMove(new Move(FRIENDLY, 3, 1))
                .withMove(new Move(FRIENDLY, 2, 2))
                .withMove(new Move(FRIENDLY, 1, 3));

        assertTrue(board.willBeTerminalCell(0, 4, 5));
    }

    @Test
    public void willBeTerminalCellWin4SouthWest() {
        //   |   |   |   |(F)| ...
        //   |   |   | F |   |   ...
        //   |   | F |   |   |   ...
        //   | F |   |   |   |   ...
        // F |   |   |   |   |   ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 3, 1))
                .withMove(new Move(FRIENDLY, 2, 2))
                .withMove(new Move(FRIENDLY, 1, 3))
                .withMove(new Move(FRIENDLY, 0, 4));

        assertTrue(board.willBeTerminalCell(4, 0, 5));
    }

    @Test
    public void willBeTerminalCellWin2NorthEast2SouthWest() {
        //   |   |   |   | F | ...
        //   |   |   | F |   |   ...
        //   |   |(F)|   |   |   ...
        //   | F |   |   |   |   ...
        // F |   |   |   |   |   ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 4, 0))
                .withMove(new Move(FRIENDLY, 3, 1))
                .withMove(new Move(FRIENDLY, 1, 3))
                .withMove(new Move(FRIENDLY, 0, 4));

        assertTrue(board.willBeTerminalCell(2, 2, 5));
    }

    @Test
    public void willBeTerminalCellWin4NorthWest() {
        // F |   |   |   |   | ...
        //   | F |   |   |   |   ...
        //   |   | F |   |   |   ...
        //   |   |   | F |   |   ...
        //   |   |   |   |(F)|   ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 2, 2))
                .withMove(new Move(FRIENDLY, 3, 3));

        assertTrue(board.willBeTerminalCell(4, 4, 5));
    }

    @Test
    public void willBeTerminalCellWin4SouthEast() {
        //(F)|   |   |   |   | ...
        //   | F |   |   |   |   ...
        //   |   | F |   |   |   ...
        //   |   |   | F |   |   ...
        //   |   |   |   | F |   ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 1, 1))
                .withMove(new Move(FRIENDLY, 2, 2))
                .withMove(new Move(FRIENDLY, 3, 3))
                .withMove(new Move(FRIENDLY, 4, 4));

        assertTrue(board.willBeTerminalCell(0, 0, 5));
    }

    @Test
    public void willBeTerminalCellWinOpponent() {
        //(F)|   |   |   |   | ...
        //   | O |   |   |   |   ...
        //   |   | O |   |   |   ...
        //   |   |   | O |   |   ...
        //   |   |   |   | O |   ...
        Board board = new Board(15, 15)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 4, 4));

        assertTrue(board.willBeTerminalCell(0, 0, 5));
    }

    @Test
    public void willBeTerminalCellFalse() {
        // F | F | F |(F)| ...
        //   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0));

        assertFalse(board.willBeTerminalCell(3, 0, 5));
    }

}