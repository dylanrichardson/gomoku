package gomoku;

import com.sun.tools.javac.util.Pair;
import java.util.*;
import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

class Board {

    static final Character COL_INDEX = 'a';
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
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
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

    Boolean willBeTerminalCell(Integer col, Integer row, Integer winLength) {
        return getNumInARowHorizontal(col, row, winLength) >= winLength - 1
                || getNumInARowVertical(col, row, winLength) >= winLength - 1
                || getNumInARowPositiveDiagonal(col, row, winLength) >= winLength - 1
                || getNumInARowNegativeDiagonal(col, row, winLength) >= winLength - 1;
    }

    private Integer getNumInARowNegativeDiagonal(Integer col, Integer row, Integer winLength) {
        return getNumInARowNorthWest(col, row, winLength) + getNumInARowSouthEast(col, row, winLength);
    }

    private Integer getNumInARowSouthEast(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, 1, 1);
    }

    private Integer getNumInARowNorthWest(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, -1, -1);
    }

    private Integer getNumInARowPositiveDiagonal(Integer col, Integer row, Integer winLength) {
        return getNumInARowNorthEast(col, row, winLength) + getNumInARowSouthWest(col, row, winLength);
    }

    private Integer getNumInARowSouthWest(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, -1, 1);
    }

    private Integer getNumInARowNorthEast(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, 1, -1);
    }

    private Integer getNumInARowVertical(Integer col, Integer row, Integer winLength) {
        return getNumInARowNorth(col, row, winLength) + getNumInARowSouth(col, row, winLength);
    }

    private Integer getNumInARowNorth(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, 0, -1);
    }

    private Integer getNumInARowSouth(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, 0, 1);
    }

    private Integer getNumInARowHorizontal(Integer col, Integer row, Integer winLength) {
        return getNumInARowWest(col, row, winLength) + getNumInARowEast(col, row, winLength);
    }

    private Integer getNumInARowEast(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, 1, 0);
    }

    private Integer getNumInARowWest(Integer col, Integer row, Integer winLength) {
        return getNumInARow(col, row, winLength, -1, 0);
    }

    private Integer getNumInARow(Integer col, Integer row, Integer winLength, Integer dCol, Integer dRow) {
        Stone stone = getStoneInCell(col + dCol, row + dRow);
        if (stone == null)
            return 0;
        int numInARow = 2;
        for (; numInARow < winLength; numInARow++) {
            if (getStoneInCell(col + dCol * numInARow, row + dRow * numInARow) != stone) {
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
}

