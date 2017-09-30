package gomoku;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static gomoku.Direction.*;
import static gomoku.Main.BOARD_SIDE_LENGTH;
import static gomoku.Main.WIN_LENGTH;
import static gomoku.Player.TIME_LIMIT;
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

    // strategies

    @Test
    public void isWin() {
        testStrategy(BoardTest::isWin, 4);
    }

    @Test
    public void isBlock() {
        testStrategy(BoardTest::isBlock, 4);
    }

    @Test
    public void is2AwayWin() {
        testStrategy(BoardTest::is2AwayWin, 3);
    }

    @Test
    public void is2AwayBlock() {
        testStrategy(BoardTest::is2AwayBlock, 3);
    }

    @Test
    public void isComboWin() {
        testComboStrategy(BoardTest::isComboWin);
    }

    @Test
    public void isComboBlock() {
        testComboStrategy(BoardTest::isComboBlock);
    }

    private void testStrategy(BiFunction<Board, Move, Boolean> isStrategy, Integer length) {
        IntStream.range(0, length + 1)
                .forEach(countDir -> forEachDirection(isStrategy, countDir, length - countDir));
    }

    private void forEachDirection(BiFunction<Board, Move, Boolean> isStrategy, Integer countDir, Integer countOppDir) {
        Direction.all().forEach(direction -> {
            Move move = new Move(FRIENDLY, BOARD_SIDE_LENGTH / 2,  BOARD_SIDE_LENGTH / 2);
            Board board = new Board(BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH)
                    .withMoves(getMovesInDirection(move, direction, countDir))
                    .withMoves(getMovesInDirection(move, direction.opposite(), countOppDir));

            String errMsg = "\n" + board + "direction: " + direction + " countDir: " + countDir;
            assertTrue(errMsg, isStrategy.apply(board, move));
        });
    }

    private Collection<Move> getMovesInDirection(Move move, Direction direction, Integer count) {
        return IntStream
                .range(1, count + 1)
                .mapToObj(distance -> move.translate(direction, distance))
                .collect(Collectors.toList());
    }

    private static Boolean isWin(Board board, Move move) {
        return board.isWin(move, WIN_LENGTH);
    }

    private static Boolean isBlock(Board board, Move move) {
        return board.isBlock(move.forOpponent(), WIN_LENGTH);
    }

    private static Boolean is2AwayWin(Board board, Move move) {
        return board.is2AwayWin(move, WIN_LENGTH);
    }

    private static Boolean is2AwayBlock(Board board, Move move) {
        return board.is2AwayBlock(move.forOpponent(), WIN_LENGTH);
    }

    private static Boolean isComboWin(Board board, Move move) {
        return board.isComboWin(move, WIN_LENGTH);
    }

    private static Boolean isComboBlock(Board board, Move move) {
        return board.isComboBlock(move.forOpponent(), WIN_LENGTH);
    }

    private void testComboStrategy(BiFunction<Board, Move, Boolean> isStrategy) {
        testComboNoSpace(isStrategy);
        testCombo1Space(isStrategy);
        testCombo2Space(isStrategy);
    }

    private void testComboNoSpace(BiFunction<Board, Move, Boolean> isStrategy) {
        Direction.all().forEach(dir1 -> {
            Direction.all().forEach(dir2 -> {
                if (!dir1.equals(dir2)) {
                    Move move = new Move(FRIENDLY, BOARD_SIDE_LENGTH / 2, BOARD_SIDE_LENGTH / 2);
                    Board board = new Board(BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH)
                            .withMoves(getMovesInDirection(move, dir1, 3))
                            .withMoves(getMovesInDirection(move, dir2, 3));

                    String errMsg = "\n" + board + "dir1: " + dir1 + "dir2: " + dir2 + " countDir: " + 3;
                    assertTrue(errMsg, isStrategy.apply(board, move));
                }
            });
        });
    }

    private void testCombo1Space(BiFunction<Board, Move, Boolean> isStrategy) {
        Direction.all().forEach(dir1 -> {
            Direction.all().forEach(dir2 -> {
                if (!dir1.equals(dir2) && !dir1.equals(dir2.opposite())) {
                    Move move = new Move(FRIENDLY, BOARD_SIDE_LENGTH / 2, BOARD_SIDE_LENGTH / 2);
                    Board board = new Board(BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH)
                            .withMoves(getMovesInDirection(move, dir1, 2))
                            .withMoves(getMovesInDirection(move, dir2, 2))
                            .withMoves(getMovesInDirection(move, dir1.opposite(), 1))
                            .withMoves(getMovesInDirection(move, dir2.opposite(), 1));

                    String errMsg = "\n" + board + "dir1: " + dir1 + "dir2: " + dir2 + " countDir: " + 3;
                    assertTrue(errMsg, isStrategy.apply(board, move));
                }
            });
        });
    }

    private void testCombo2Space(BiFunction<Board, Move, Boolean> isStrategy) {
        Direction.all().forEach(dir1 -> {
            Direction.all().forEach(dir2 -> {
                if (!dir1.equals(dir2) && !dir1.equals(dir2.opposite())) {
                    Move move = new Move(FRIENDLY, BOARD_SIDE_LENGTH / 2, BOARD_SIDE_LENGTH / 2);
                    Board board = new Board(BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH)
                            .withMoves(getMovesInDirection(move.translate(dir1), dir1, 2))
                            .withMoves(getMovesInDirection(move.translate(dir2), dir2, 2))
                            .withMoves(getMovesInDirection(move, dir1.opposite(), 1))
                            .withMoves(getMovesInDirection(move, dir2.opposite(), 1));

                    String errMsg = "\n" + board + "dir1: " + dir1 + "dir2: " + dir2 + " countDir: " + 3;
                    assertTrue(errMsg, isStrategy.apply(board, move));
                }
            });
        });
    }

    @Test
    public void border() {
        // O |   |
        // O |   |
        // O |   |
        //(F)|   |
        //   |   |
        Board board = new Board(2, 7)
                .withMove(new Move(OPPONENT, 0, 0))
                .withMove(new Move(OPPONENT, 0, 1))
                .withMove(new Move(OPPONENT, 0, 2));

        Move move = new Move(FRIENDLY, 0, 3);
        assertTrue(board.is2AwayWinOnAxis(move.forOpponent(), 5, North));
        assertTrue(board.is2AwayBlock(move, 5));
    }

}