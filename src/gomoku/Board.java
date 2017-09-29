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

    Collection<Pair<Integer, Integer>> getOpenCells() {
        List<Pair<Integer, Integer>> openCells = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (cells[col][row] == null) {
                    openCells.add(new Pair<>(col, row));
                }
            }
        }
        return openCells;
    }

    Stone getStoneInCell(Integer column, Integer row) {
        return (column >= 0 && column < width && row >= 0 && row < height) ? cells[column][row] : null;
    }

    Boolean isValidMove(Move move) {
        return move != null && isWithinBounds(move.getColumn(), move.getRow());
    }

    Boolean isDraw() {
        return getOpenCells().isEmpty();
    }

    Boolean isWin(Move move, Integer winLength) {
        return Direction.semiCircle().anyMatch(dir -> isWinInDirection(move, winLength, dir));
    }

    private Boolean isWinInDirection(Move move, Integer winLength, Direction direction) {
        return countConsecutiveStones(move, winLength, direction)
                + countConsecutiveStones(move, winLength, direction.opposite())
                >= winLength - 1;
    }

    Boolean isBlock(Move move, Integer winLength) {
        return isWin(move.forOpponent(), winLength);
    }

    Boolean is2AwayWin(Move move, Integer winLength) {
        return Direction.semiCircle().anyMatch(dir -> is2AwayWinInDirection(move, winLength, dir));
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

    Boolean hasStoneInCell(Integer col, Integer row) {
        return getStoneInCell(col, row) != null;
    }

    private Boolean is2AwayWinInDirection(Move move, Integer winLength, Direction direction) {
        int col = move.getColumn() + winLength * direction.dCol;
        int row = move.getRow() + winLength * direction.dRow;
        int oppLength = winLength - 2;
        if (isWithinBounds(col, row)) {
            Integer countInDir = countConsecutiveStones(move, oppLength, direction);
            Integer countInOppDir = countConsecutiveStones(move, oppLength, direction.opposite());
            if (countInDir >= oppLength) {
                return !hasStoneInCell(col, row) && !hasStoneInCell(col - direction.dCol, row - direction.dRow);
            }
        }
        return false;
    }

    private Boolean isWithinBounds(Integer col, Integer row) {
        return     0 <= col && col < width
                && 0 <= row && row < height;
    }

    private Integer countConsecutiveStones(Move move, Integer length, Direction direction) {
        if (length == 1)
            return 0;
        for (int count = 1; count < length; count++) {
            int newCol = move.getColumn() + direction.dCol * count;
            int newRow = move.getRow() + direction.dRow * count;
            if (getStoneInCell(newCol, newRow) != move.getStone()) {
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

