package gomoku;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void withMove() {
        String playerName = "test";
        Board board = new Board(3, 3)
                .withMove(new Move(playerName, 0, 0));
        assertEquals(playerName, board.getPlayerInCell(0, 0));
    }

    @Test
    public void withMoveOnMove() {
        String secondPlayer = "second";
        Board board = new Board(3, 3)
                .withMove(new Move("first", 0, 0))
                .withMove(new Move(secondPlayer, 0, 0));
        assertEquals(secondPlayer, board.getPlayerInCell(0, 0));
    }

    @Test
    public void getOpenCells() {
        String playerName = "test";
        Board board = new Board(3, 3)
                .withMove(new Move(playerName, 0, 0));
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
    public void isTerminalTrue() {
        String playerName = "test";
        Board board = new Board(3, 3)
                .withMove(new Move(playerName, 0, 0))
                .withMove(new Move(playerName, 0, 1))
                .withMove(new Move(playerName, 0, 2));
        assertTrue(board.isTerminal(3));
    }

    @Test
    public void isTerminalFalse() {
        assertFalse(new Board().isTerminal(3));
    }

}