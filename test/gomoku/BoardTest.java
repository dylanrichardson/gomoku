package gomoku;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

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

    // willBeWin

    @Test
    public void willBeWinWin4West() {
        // F | F | F | F |(F)| ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0));

        assertTrue(board.willBeWin(new Move(FRIENDLY,4, 0), 5));
    }

    @Test
    public void willBeWinWin4East() {
        //(F)| F | F | F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void willBeWinWin3West1East() {
        // F | F | F |(F)| F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeWin(new Move(FRIENDLY,3, 0), 5));
    }

    @Test
    public void willBeWinWin2West2East() {
        // F | F |(F)| F | F | ...
        //   |   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 3, 0))
                .withMove(new Move(FRIENDLY, 4, 0));

        assertTrue(board.willBeWin(new Move(FRIENDLY,2, 0), 5));
    }

    @Test
    public void willBeWinWin4North() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 4), 5));
    }

    @Test
    public void willBeWinWin4South() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void willBeWinWin2North2South() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 2), 5));
    }

    @Test
    public void willBeWinWin4NorthEast() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 4), 5));
    }

    @Test
    public void willBeWinWin4SouthWest() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,4, 0), 5));
    }

    @Test
    public void willBeWinWin2NorthEast2SouthWest() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,2, 2), 5));
    }

    @Test
    public void willBeWinWin4NorthWest() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,4, 4), 5));
    }

    @Test
    public void willBeWinWin4SouthEast() {
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

        assertTrue(board.willBeWin(new Move(FRIENDLY,0, 0), 5));
    }

    @Test
    public void willBeWinFalse() {
        // F | F | F |(F)| ...
        //   |   |   |   | ...
        // ...
        Board board = new Board(15, 15)
                .withMove(new Move(FRIENDLY, 0, 0))
                .withMove(new Move(FRIENDLY, 1, 0))
                .withMove(new Move(FRIENDLY, 2, 0));

        assertFalse(board.willBeWin(new Move(FRIENDLY,3, 0), 5));
    }

    @Test
    public void willBeBlockIn2MovesEast() {
        //   |   |   |   |   |
        //(F)| O | O | O |   |
        //   |   |   |   |   |
        Board board = new Board(6, 3)
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 1));

        Move move = new Move(FRIENDLY, 0, 1);
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesWest() {
        //   |   |   |   |   |
        //   |   | O | O | O |(F)
        //   |   |   |   |   |
        Board board = new Board(6, 3)
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 1))
                .withMove(new Move(OPPONENT, 4, 1));

        Move move = new Move(FRIENDLY, 5, 1);
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesNorth() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesSouth() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesNorthEast() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesSouthWest() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesNorthWest() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    @Test
    public void willBeBlockIn2MovesSouthEast() {
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
        assertTrue(board.willBeBlockIn2Moves(move, 5));
    }

    // intersection of 2 2-away wins
    // opponent move would produce 2 ways to win

    @Test
    public void willBeComboBlockWestAndSouth() {
        Debug.on();
        //   |   |   |
        // O | O | O |(F)
        //   |   |   | O
        //   |   |   | O
        //   |   |   | O
        Board board = new Board(4, 5)
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 1, 1))
                .withMove(new Move(OPPONENT, 2, 1))
                .withMove(new Move(OPPONENT, 3, 2))
                .withMove(new Move(OPPONENT, 3, 3))
                .withMove(new Move(OPPONENT, 3, 4));

        Move move = new Move(FRIENDLY, 3, 1);
        assertTrue(board.willBeComboBlock(move, 5));
    }

}