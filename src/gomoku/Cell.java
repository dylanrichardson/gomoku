package gomoku;

import static gomoku.Board.printColumn;
import static gomoku.Board.printRow;

class Cell {


    private final Integer column;
    private final Integer row;

    Cell(Integer column, Integer row) {
        this.column = column;
        this.row = row;
    }

    Integer getColumn() {
        return column;
    }

    Integer getRow() {
        return row;
    }

    Cell translate(Direction direction, int distance) {
        return new Cell(column + direction.dCol * distance, row + direction.dRow * distance);
    }

    Cell translate(Direction direction) {
        return translate(direction, 1);
    }

    @Override
    public String toString() {
        return printColumn(column) + " " + printRow(row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return (column != null ? column.equals(cell.column) : cell.column == null) && (row != null ? row.equals(cell.row) : cell.row == null);
    }

    @Override
    public int hashCode() {
        int result = column != null ? column.hashCode() : 0;
        result = 31 * result + (row != null ? row.hashCode() : 0);
        return result;
    }
}
