package gomoku;

import static gomoku.Board.convertColumn;
import static gomoku.Board.convertRow;

class Move {

    private final Stone stone;
    private final Cell cell;

    // when parsed
    Move(Stone stone, Character column, Integer row) {
        this(stone, convertColumn(column), convertRow(row));
    }

    // internal representation
    Move(Stone stone, Integer column, Integer row) {
        this(stone, new Cell(column, row));
    }

    Move(Stone stone, Cell cell) {
        this.stone = stone;
        this.cell = cell;
    }

    @Override
    public String toString() {
        return stone + " " + cell;
    }

    Cell getCell() {
        return cell;
    }

    Integer getColumn() {
        return cell.getColumn();
    }

    Stone getStone() {
        return stone;
    }

    Integer getRow() {
        return cell.getRow();
    }



    Move forOpponent() {
        return new Move(stone.getOpponent(), getColumn(), getRow());
    }

    Move translate(Direction direction, Integer distance) {
        return new Move(stone, cell.translate(direction, distance));
    }

    Move translate(Direction direction) {
        return translate(direction, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        return stone == move.stone && (cell != null ? cell.equals(move.cell) : move.cell == null);
    }

    @Override
    public int hashCode() {
        int result = stone != null ? stone.hashCode() : 0;
        result = 31 * result + (cell != null ? cell.hashCode() : 0);
        return result;
    }
}
