package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static gomoku.Direction.North;
import static gomoku.Direction.South;
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
        return move != null
                && move.getColumn() < width
                && move.getColumn() >= 0
                && move.getRow() < height
                && move.getRow() >= 0;
    }

    Boolean isDraw() {
        return getOpenCells().isEmpty();
    }

    Boolean willBeWin(Move move, Integer winLength) {
        Integer minLength = winLength - 1;
        return Direction.semiCircle().anyMatch(dir ->
                getNumInARow(move, winLength, dir) + getNumInARow(move, winLength, dir.flip()) >= minLength);
    }

    Boolean willBeBlock(Move move, Integer winLength) {
        return willBeWin(move.forOpponent(), winLength);
    }

    Boolean willBeBlockIn2Moves(Move move, Integer winLength) {
        return Direction.all().anyMatch(dir -> {
            int col = move.getColumn() + winLength * dir.dCol;
            int row = move.getRow() + winLength * dir.dRow;
            int oppLength = winLength - 2;
            return willBeBlockIn2MovesInDirection(move.forOpponent(), col, row, oppLength, dir);
        });
    }

    private Boolean willBeBlockIn2MovesInDirection(Move move, Integer col, Integer row, Integer length, Direction direction) {
        return     isWithinBounds(col, row)
                && getNumInARow(move, length, direction).equals(length)
                && getStoneInCell(col, row) == null
                && getStoneInCell(col - direction.dCol, row - direction.dRow) == null;
    }

    private Boolean isWithinBounds(Integer col, Integer row) {
        return     0 <= col && col < width
                && 0 <= row && row < height;
    }

    private Integer getNumInARow(Move move, Integer length, Direction direction) {
        if (length == 1)
            return 0;
        int numInARow = 1;
        for (; numInARow < length; numInARow++) {
            int newCol = move.getColumn() + direction.dCol * numInARow;
            int newRow = move.getRow() + direction.dRow * numInARow;
            if (getStoneInCell(newCol, newRow) != move.getStone()) {
                numInARow--;
                break;
            }
        }
        return numInARow;
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

    Boolean willBeComboBlock(Move move, Integer winLength) {
        Board board = withMove(move.forOpponent());
        Integer minLength = winLength - 1;
        Move northMove = new Move(move.getStone().getOpponent(), move.getColumn() + North.dCol, move.getRow() + North.dRow);
        Integer numInARow = getNumInARow(northMove, winLength, South);
        if (numInARow >= minLength)
            return true;
        return false;
    }
}

