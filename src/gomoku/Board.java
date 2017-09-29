package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static gomoku.Direction.East;
import static gomoku.Direction.North;
import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

class Board {

    private static final Character COL_INDEX = 'a';
    private static final int ROW_INDEX = 1;

    private final Stone[][] cells;
    private final Integer width;
    private final Integer height;

    private Board(Integer width, Integer height, Stone[][] cells) {
        this.cells = cells;
        this.width = width;
        this.height = height;
    }

    static String printColumn(int col) {
        return toUpperCase((char) (col + COL_INDEX)) + "";
    }

    static String printRow(Integer row) {
        return Integer.toString(row + ROW_INDEX);
    }

    static Integer convertColumn(Character column) {
        return toLowerCase(column) - COL_INDEX;
    }

    static Integer convertRow(Integer row) {
        return row - ROW_INDEX;
    }

    Board(Integer width, Integer height) {
        this(width, height, new Stone[width][height]);
    }

    Board withMove(Move move) {
        Stone[][] newCells = Arrays
                .stream(cells)
                .map(Stone[]::clone)
                .toArray(Stone[][]::new);
        if (move != null)
            newCells[move.getColumn()][move.getRow()] = move.getStone();
        return new Board(width, height, newCells);
    }

    Collection<Cell> getOpenCells() {
        List<Cell> openCells = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (cells[col][row] == null) {
                    openCells.add(new Cell(col, row));
                }
            }
        }
        return openCells;
    }

    Stone getStoneInCell(Cell cell) {
        return isWithinBounds(cell) ? cells[cell.getColumn()][cell.getRow()] : null;
    }

    Boolean isValidMove(Move move) {
        return move != null && isWithinBounds(move.getCell());
    }

    Boolean isDraw() {
        return getOpenCells().isEmpty();
    }

    Boolean isWin(Move move, Integer winLength) {
        return Direction.semiCircle().anyMatch(dir -> isWinOnAxis(move, winLength, dir));
    }

    private Boolean isWinOnAxis(Move move, Integer winLength, Direction direction) {
        return countConsecutiveStones(move, winLength, direction)
                + countConsecutiveStones(move, winLength, direction.opposite())
                >= winLength - 1;
    }

    Boolean isBlock(Move move, Integer winLength) {
        return isWin(move.forOpponent(), winLength);
    }

    Boolean is2AwayWin(Move move, Integer winLength) {
        return Direction.semiCircle().anyMatch(dir -> is2AwayWinOnAxis(move, winLength, dir));
    }

    Boolean is2AwayBlock(Move move, Integer winLength) {
        return is2AwayWin(move.forOpponent(), winLength);
    }

    Boolean isComboWin(Move move, Integer winLength) {
        Board board = withMove(move);
        Integer minLength = winLength - 1;
        return board.countConsecutiveStones(move.translate(North), winLength, North.opposite()) >= minLength
                || board.countConsecutiveStones(move.translate(East), winLength, East.opposite()) >= minLength;
    }

    Boolean isComboBlock(Move move, Integer winLength) {
        return isComboWin(move.forOpponent(), winLength);
    }

    Integer getWidth() {
        return width;
    }

    Integer getHeight() {
        return height;
    }

    Boolean hasStoneInCell(Cell cell) {
        return getStoneInCell(cell) != null;
    }

    private Boolean is2AwayWinOnAxis(Move move, Integer winLength, Direction direction) {
        return is2AwayWinInDirection(move, winLength, direction)
                || is2AwayWinInDirection(move, winLength, direction.opposite());
    }

    private Boolean is2AwayWinInDirection(Move move, Integer winLength, Direction direction) {
        int oppLength = winLength - 2;
        Cell endCell = move.getCell().translate(direction, winLength);
        if (isWithinBounds(endCell)) {
            Integer countInDir = countConsecutiveStones(move, oppLength, direction);
            if (countInDir >= oppLength) {
                return !hasStoneInCell(endCell)
                        && !hasStoneInCell(move.getCell().translate(direction, winLength - 1));
            }
        }
        return false;
    }

    private Boolean isWithinBounds(Cell cell) {
        return     0 <= cell.getColumn() && cell.getColumn() < width
                && 0 <= cell.getRow() && cell.getRow() < height;
    }

    private Integer countConsecutiveStones(Move move, Integer length, Direction direction) {
        if (length == 1)
            return 0;
        for (int count = 1; count < length; count++) {
            if (getStoneInCell(move.getCell().translate(direction, count)) != move.getStone()) {
                return count - 1;
            }
        }
        return length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(printColumnHeaders());
        for (int row = 0; row < height; row++) {
            stringBuilder.append(String.format("%2s", printRow(row)));
            for (int col = 0; col < width - 1; col++) {
                stringBuilder.append(" ");
                stringBuilder.append(printCell(cells[col][row]));
                stringBuilder.append(" |");
            }
            stringBuilder.append(" ");
            stringBuilder.append(printCell(cells[width - 1][row]));
            stringBuilder.append(" \n");
        }
        return stringBuilder.toString();
    }

    private String printColumnHeaders() {
        StringBuilder stringBuilder = new StringBuilder(" ");
        for (int col = 0; col < width; col++) {
            stringBuilder
                    .append("  ")
                    .append(printColumn(col))
                    .append(" ");
        }
        return stringBuilder.append("\n").toString();
    }

    private String printCell(Stone stone) {
        if (stone == FRIENDLY)
            return "F";
        if (stone == OPPONENT)
            return "O";
        return " ";
    }
}

