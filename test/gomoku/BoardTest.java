package gomoku;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static org.junit.Assert.*;

public class BoardTest {

    // withMove

    @Test
    public void withMove3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        assertEquals(FRIENDLY, board.getStoneInCell(new Cell(0, 0)));
    }
    @Test
    public void withMoveNoMutation3x3() {
        Board board1 = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        board1.withMove(new Move(OPPONENT, 0, 0));
        assertEquals(FRIENDLY, board1.getStoneInCell(new Cell(0, 0)));
    }

    @Test
    public void withMoveOnMove3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 0, 0));
        assertEquals(OPPONENT, board.getStoneInCell(new Cell(0, 0)));
    }

    // getOpenCells

    @Test
    public void getOpenCells3x3() {
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0));
        Set<Cell> openCells = new HashSet<>(Arrays.asList(
                new Cell(0,1),
                new Cell(0,2),
                new Cell(1,0),
                new Cell(1,1),
                new Cell(1,2),
                new Cell(2,0),
                new Cell(2,1),
                new Cell(2,2)));

        assertEquals(openCells,new HashSet<>(board.getOpenCells()));
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
        String boardString =    "   A   B   C \n"
                           + " 1 F | F | O \n"
                           + " 2 O |   |   \n"
                           + " 3   |   |   \n";
        Board board = new Board(3, 3)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(OPPONENT, 2, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(OPPONENT, 0, 1));
        assertEquals(boardString, board.toString());
    }

    // isWin

    @Test
    public void isWinWest() {
        // F | F | F | F |(F)| ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0));

        assertTrue(board.isWin(new Move(FRIENDLY,4, 0), 5));
    }

    @Test
    public void isWinEast() {
        //(F)| F | F | F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.isWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void isWin3West1East() {
        // F | F | F |(F)| F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.isWin(new Move(FRIENDLY,3, 0), 5));
    }

    @Test
    public void isWin2West2East() {
        // F | F |(F)| F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.isWin(new Move(FRIENDLY,2, 0), 5));
    }

    @Test
    public void isWinNorth() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,0, 4), 5));
    }

    @Test
    public void isWinSouth() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void isWin2North2South() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,0, 2), 5));
    }

    @Test
    public void isWinNorthEast() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,0, 4), 5));
    }

    @Test
    public void isWinSouthWest() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,4, 0), 5));
    }

    @Test
    public void isWin2NorthEast2SouthWest() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,2, 2), 5));
    }

    @Test
    public void isWinNorthWest() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,4, 4), 5));
    }

    @Test
    public void isWinSouthEast() {
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

        assertTrue(board.isWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void isWinFalse() {
        // F | F | F |(F)| ...
        //   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0));

        assertFalse(board.isWin(new Move(FRIENDLY,3, 0), 5));
    }

    @Test
    public void is2AwayBlockEast() {
        //   |   |   |   |   |
        //(F)| O | O | O |   |
        //   |   |   |   |   |
        Board board = new Board(6, 3)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 1));

        Move move = new Move(FRIENDLY, 0, 1);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockWest() {
        //   |   |   |   |   |
        //   |   | O | O | O |(F)
        //   |   |   |   |   |
        Board board = new Board(6, 3)
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 1))
                .withMove(new Move(OPPONENT, 4, 1));

        Move move = new Move(FRIENDLY, 5, 1);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockNorth() {
        //   |   |
        //   |   |
        //   | O |
        //   | O |
        //   | O |
        //   |(F)|
        Board board = new Board(3, 6)
                .withMove(new Move(OPPONENT, 1, 2))
                .withMove(new Move(OPPONENT, 1, 3))
                .withMove(new Move(OPPONENT, 1, 4));

        Move move = new Move(FRIENDLY, 1, 5);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockSouth() {
        //   |(F)|
        //   | O |
        //   | O |
        //   | O |
        //   |   |
        //   |   |
        Board board = new Board(3, 6)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 1, 2))
                .withMove(new Move(OPPONENT, 1, 3));

        Move move = new Move(FRIENDLY, 1, 0);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockNorthEast() {
        //   |   |   |   |   |
        //   |   |   |   |   |
        //   |   |   | O |   |
        //   |   | O |   |   |
        //   | O |   |   |   |
        //(F)|   |   |   |   |
        Board board = new Board(6, 6)
                .withMove(new Move(OPPONENT, 1, 4))
                .withMove(new Move(OPPONENT, 2, 3))
                .withMove(new Move(OPPONENT, 3, 2));

        Move move = new Move(FRIENDLY, 0, 5);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockSouthWest() {
        //   |   |   |   |   |(F)
        //   |   |   |   | O |
        //   |   |   | O |   |
        //   |   | O |   |   |
        //   |   |   |   |   |
        //   |   |   |   |   |
        Board board = new Board(6, 6)
                .withMove(new Move(OPPONENT, 2, 3))
                .withMove(new Move(OPPONENT, 3, 2))
                .withMove(new Move(OPPONENT, 4, 1));

        Move move = new Move(FRIENDLY, 5, 0);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockNorthWest() {
        //   |   |   |   |   |
        //   |   |   |   |   |
        //   |   | O |   |   |
        //   |   |   | O |   |
        //   |   |   |   | O |
        //   |   |   |   |   | F
        Board board = new Board(6, 6)
                .withMove(new Move(OPPONENT, 2, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 4, 4));

        Move move = new Move(FRIENDLY, 5, 5);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockSouthEast() {
        //(F)|   |   |   |   |
        //   | O |   |   |   |
        //   |   | O |   |   |
        //   |   |   | O |   |
        //   |   |   |   |   |
        //   |   |   |   |   |
        Board board = new Board(6, 6)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 2))
                .withMove(new Move(OPPONENT, 3, 3));

        Move move = new Move(FRIENDLY, 0, 0);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void is2AwayBlockNorthAndSouth() {
        //   |   |   |   |   |
        //   |   |   | O |   |
        //   |   |   |(F)|   |
        //   |   |   | O |   |
        //   |   |   | O |   |
        //   |   |   |   |   |
        Board board = new Board(6, 6)
                .withMove(new Move(OPPONENT, 3, 1))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 3, 4));

        Move move = new Move(FRIENDLY, 3, 2);
        assertTrue(board.is2AwayBlock(move, 5));
    }

    @Test
    public void isComboBlockWestAndSouth() {
        //   |   |   |   |
        // O | O | O |(F)|
        //   |   |   | O |
        //   |   |   | O |
        //   |   |   | O |
        Board board = new Board(5, 5)
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 3, 4));

        Move move = new Move(FRIENDLY, 3, 1);
        assertTrue(board.isComboBlock(move, 5));
    }

    @Test
    public void isComboBlockWestAndSouth2() {
        // O | O | O |(F)|
        //   |   |   |   |
        //   |   |   | O |
        //   |   |   | O |
        //   |   |   | O |
        Board board = new Board(5, 5)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 1, 0))
                .withMove(new Move(OPPONENT, 2, 0))
                .withMove(new Move(OPPONENT, 3, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 3, 4));

        Move move = new Move(FRIENDLY, 3, 0);
        assertTrue(board.isComboBlock(move, 5));
    }

}