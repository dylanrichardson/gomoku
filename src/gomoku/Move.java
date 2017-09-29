package gomoku;

import static gomoku.Board.*;
import static gomoku.Stone.FRIENDLY;

class Move {

    private final Stone stone;
    private final Integer column;
    private final Integer row;

    // when parsed
    Move(Stone stone, Character column, Integer row) {
        this(stone, convertColumn(column), convertRow(row));
    }

    // internal representation
    Move(Stone stone, Integer column, Integer row) {
        this.stone = stone;
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return ((stone == FRIENDLY) ? "F" : "O") + " " + getCell();
    }

    String getCell() {
        return printColumn(column) + " " + printRow(row);
    }

    Integer getColumn() {
        return column;
    }

    Stone getStone() {
        return stone;
    }

    Integer getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        return stone == move.stone
                && (column != null ? column.equals(move.column) : move.column == null)
                && (row != null ? row.equals(move.row) : move.row == null);
    }

    @Override
    public int hashCode() {
        int result = stone != null ? stone.hashCode() : 0;
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (row != null ? row.hashCode() : 0);
        return result;
    }

    Move forOpponent() {
        return new Move(stone.getOpponent(), column, row);
    }

    Move translate(Direction direction) {
        return new Move(stone, column + direction.dCol, row + direction.dRow);
    }
}
